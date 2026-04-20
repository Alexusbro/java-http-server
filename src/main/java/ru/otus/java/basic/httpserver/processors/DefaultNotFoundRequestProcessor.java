package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

public class DefaultNotFoundRequestProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(output);
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("<html><body><h1>" + "Page not found" + "</h1></body></html>");
        response.send();
    }
}
