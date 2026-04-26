package ru.otus.java.basic.httpserver;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.httpserver.Exeption.PayloadTooLargeException;
import ru.otus.java.basic.httpserver.Exeption.ResourceNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class.getName());
    private int port;
    private String host;
    private int threadPoolSize;
    private Dispatcher dispatcher;
    private PropertiesServer propertiesServer;

    ExecutorService threadPools;

    public HttpServer() {
        propertiesServer = new PropertiesServer();
        propertiesServer.setProperties();
        this.port = Integer.parseInt(propertiesServer.getPort());
        this.host = propertiesServer.getHost();
        this.threadPoolSize = Integer.parseInt(propertiesServer.getThreadPoolSize());
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        threadPools = Executors.newFixedThreadPool(threadPoolSize);
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host))) {
            logger.log(Level.INFO, "server started");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    logger.info("new client connected");
                    threadPools.execute(() -> {
                        try (socket) {
                            try {
                                HttpRequest request = new HttpRequest(socket.getInputStream(), propertiesServer);
                                request.info(true);
                                dispatcher.execute(request, socket.getOutputStream());
                            } catch (PayloadTooLargeException e) {
                                sendError(socket.getOutputStream(), HttpStatus.PAYLOAD_TOO_LARGE, "error413.html");
                                logger.warn("request size is too large, " + Thread.currentThread(), e);
                            } catch (ResourceNotFoundException e) {
                                sendError(socket.getOutputStream(), HttpStatus.NOT_FOUND, "error404.html");
                            } catch (Exception e) {
                                sendError(socket.getOutputStream(), HttpStatus.INTERNAL_SERVER_ERROR, "error500.html");
                                logger.error("error processing request " + Thread.currentThread(), e);
                            }
                        } catch (IOException e) {
                            logger.error("error client connection " + Thread.currentThread(), e);
                        }

                    });
                } catch (IOException e) {
                    logger.error("server socket error", e);
                }
            }
        } catch (IOException e) {
            logger.error("failed to start server on port " + port, e);
        }
    }

    private void sendError(OutputStream outputStream, HttpStatus status, String fileName) {
        try {
            HttpResponse response = new HttpResponse(outputStream);
            response.setStatus(status);
            String strHtml = Files.readString(Paths.get("templates/" + fileName));
            response.setBody(strHtml);
            response.send();
        } catch (IOException e) {
            logger.error("failed to read template error or send response");
        }
    }
}