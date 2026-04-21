package ru.otus.java.basic.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private HttpStatus status;
    private OutputStream output;
    private String contentType;
    public static final String CONTENT_JSON = "application/json";
    public static final String CONTENT_HTML = "text/html";
    private byte[] body;

    public HttpResponse(OutputStream output) {
        this.output = output;
        this.status = HttpStatus.OK;
        this.contentType = CONTENT_HTML;
        this.body = new byte[0];
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(String rawbody) {
        this.body = rawbody.getBytes(StandardCharsets.UTF_8);
    }

    public void setBody(byte[] rawBody, String type) {
        this.contentType = type;
        this.body = rawBody;
    }

    public void send() throws IOException {
        StringBuilder header = new StringBuilder();
        header.append("HTTP/1.1 ").append(status.getCode()).append(" ").append(status.getMessage()).append("\r\n")
                .append("Content-length: ").append(body.length).append("\r\n")
                .append("Content-Type: ").append(contentType).append("\r\n\r\n");

        output.write(header.toString().getBytes(StandardCharsets.UTF_8));
        output.write(body);
        output.flush();
    }
}
