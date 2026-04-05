package ru.otus.java.basic.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class.getName());

    public static void main(String[] args) {
        new HttpServer(8189).start();
    }
}