package ru.otus.java.basic.httpserver;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class.getName());
    private int port;
    private Dispatcher dispatcher;

    ExecutorService threadPools = Executors.newFixedThreadPool(5);

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log(Level.INFO, "server started");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    logger.info("new client connected");

                    threadPools.execute(() -> {
                        try (socket) {
                            byte[] buffer = new byte[8192];
                            int n = socket.getInputStream().read(buffer);
                            if (n < 1) {
                                return;
                            }
                            String rawRequest = new String(buffer, 0, n);
                            HttpRequest request = new HttpRequest(rawRequest);
                            request.info(true);
                            dispatcher.execute(request, socket.getOutputStream());
                        } catch (IOException e) {
                            logger.error("IO exception", e);
                        }
                        ;
                    });
                } catch (IOException e) {
                    logger.error("client disconnected", e);
                }
            }
        } catch (IOException e) {
            logger.error("failed to start server on port 8189", e);
        }
    }
}