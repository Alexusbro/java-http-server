package ru.otus.java.basic.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.httpserver.Exeption.PayloadTooLargeException;

public class HttpRequest {
    private HttpMethod method;
    private String uri;
    private Map<String, String> params;
    private Map<String, String> headers;
    private byte[] body;
    private InputStream inputStream;
    private PropertiesServer propertiesServer;
    private int maxRequestSize;
    private static Logger logger = LogManager.getLogger(HttpRequest.class.getName());

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return Arrays.toString(body);
    }

    public String getRoutingUri() {
        return method + " " + uri;
    }

    public String getParameters(String key) {
        return params.get(key);
    }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    public HttpRequest(InputStream inputStream, PropertiesServer propertiesServer) throws IOException {
        this.inputStream = inputStream;
        this.propertiesServer = propertiesServer;
        maxRequestSize = Integer.parseInt(propertiesServer.getMaxRequestSize());
        this.parse();
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            System.out.println("Method: " + method);
            System.out.println("URI: " + uri);
            System.out.println("Parameters: " + params);
            System.out.println("Body: " + body);
            if (body != null) {
                System.out.println("Content-length: " + getHeader("content-length"));
            }
        }
    }

    private String readHeder(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = input.read()) != -1) {
            if (b == '\r') {
                input.read();
                break;
            }
            sb.append((char) b);
        }
        return sb.toString();
    }

    private void parse() throws IOException {
        String header = readHeder(inputStream);
        if (header.isBlank()) {
            logger.warn("uncorrected header");
            throw new IOException("uncorrected header");
        }
        String[] strMethodUri = header.split(" ");
        method = HttpMethod.valueOf(strMethodUri[0]);
        uri = strMethodUri[1];
        params = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keyValues = elements[1].split("[&]");
            for (String o : keyValues) {
                String[] keyValue = o.split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }
        headers = new HashMap<>();
        while (!(header = readHeder(inputStream)).isBlank()) {
            String[] headArr = header.split(": ", 2);
            headers.put(headArr[0].toLowerCase(), headArr[1].trim());
        }
        String contentLength = headers.get("content-length");

        if (contentLength != null) {
            int bodyLength = Integer.parseInt(contentLength);
            if (bodyLength > maxRequestSize) {
                logger.warn("payload of request too large");
                throw new PayloadTooLargeException("payload of request too large");
            }
            body = new byte[bodyLength];
            int totalRead = 0;
            while (totalRead < bodyLength) {
                int bytesToRead = bodyLength - totalRead;
                int read = inputStream.read(body, totalRead, bytesToRead);
                if (read < 1) {
                    logger.warn("request body reading aborted");
                    throw new IOException("request body reading aborted");
                }
                totalRead += read;
            }
        }
    }
}
  