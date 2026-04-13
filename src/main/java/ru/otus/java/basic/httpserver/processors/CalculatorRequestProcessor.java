package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        int a = Integer.parseInt(request.getParameters("a"));
        int b = Integer.parseInt(request.getParameters("b"));
        HttpResponse response = new HttpResponse(output);
        String result = a + " + " + b + " = " + (a + b);
        response.setBody("<html><body><h1>" + result + "</h1></body></html>");
        response.send();
    }
}
