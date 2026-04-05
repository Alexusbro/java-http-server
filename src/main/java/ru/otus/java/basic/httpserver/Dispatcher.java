package ru.otus.java.basic.httpserver;

import ru.otus.java.basic.httpserver.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private RequestProcessor defaultNotFoundRequestProcessor;
    private Map<String, RequestProcessor> processors;

    public Dispatcher() {
        this.processors = new HashMap<>();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.processors.put("GET /calculate", new CalculatorRequestProcessor());
        this.processors.put("GET /hello", new HelloRequestProcessor());
        this.processors.put("GET /item", new GetItemsRequestProcessor());
        this.processors.put("POST /item", new CreateItemsRequestProcessor());
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (!processors.containsKey(request.getRoutingUri())) {
            defaultNotFoundRequestProcessor.execute(request, output);
            return;
        }
        processors.get(request.getRoutingUri()).execute(request, output);
    }
}
