package ru.otus.java.basic.httpserver;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class.getName());
    private int port;
    private String host;
    private Dispatcher dispatcher;
    private PropertiesServer propertiesServer;

    ExecutorService threadPools = Executors.newFixedThreadPool(5);

    public HttpServer() {
        propertiesServer = new PropertiesServer();
        propertiesServer.setProperties();
        this.port = Integer.parseInt(propertiesServer.getPort());
        this.host = propertiesServer.getHost();
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host))) {
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
                        } catch (Exception e) {
                            logger.error("Error processing request, " + Thread.currentThread(), e);
                        }
                    });
                } catch (IOException e) {
                    logger.error("server socket error", e);
                }
            }
        } catch (IOException e) {
            logger.error("failed to start server on port 8189", e);
        }
    }
}