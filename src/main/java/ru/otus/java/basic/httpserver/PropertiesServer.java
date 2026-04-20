package ru.otus.java.basic.httpserver;

import java.io.FileInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertiesServer {
    private static final Logger logger = LogManager.getLogger(PropertiesServer.class.getName());
    private String port;
    private String host;

    Properties properties = new Properties();

    public void setProperties() {

        try (FileInputStream fileInput = new FileInputStream("config.properties")) {
            properties.load(fileInput);
            port = properties.getProperty("server.port");
            host = properties.getProperty("server.host");
            logger.info("configuration successful load");
        } catch (IOException e) {
            logger.warn("failed to load configuration");
            port = "8189";
            host = "0.0.0.0";
        }

    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
