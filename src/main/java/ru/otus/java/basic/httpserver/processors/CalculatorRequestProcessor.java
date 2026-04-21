package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.httpserver.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

public class CalculatorRequestProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(CalculatorRequestProcessor.class);

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        int a;
        int b;
        try {
            a = Integer.parseInt(request.getParameters("a"));
            b = Integer.parseInt(request.getParameters("b"));
        } catch (Exception e) {
            HttpResponse response = new HttpResponse(output);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("<html><body><h1>" + "Bad request, invalid parameters" + "</h1></body></html>");
            response.send();
            logger.warn("Bad request", e);
            return;
        }
        HttpResponse response = new HttpResponse(output);
        String result = a + " + " + b + " = " + (a + b);
        response.setBody("<html><body><h1>" + result + "</h1></body></html>");
        response.send();
    }
}
