package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HelloRequestProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(output);
        response.setBody("<html><body><h1>Hello World!</h1></body></html>");
        response.send();
    }
}
