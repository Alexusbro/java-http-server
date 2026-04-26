package ru.otus.java.basic.httpserver;

import ru.otus.java.basic.httpserver.Exeption.ResourceNotFoundException;
import ru.otus.java.basic.httpserver.app.ItemsService;
import ru.otus.java.basic.httpserver.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private RequestProcessor defaultNotFoundRequestProcessor;
    private DefaultStaticResourceProcessor defaultStaticRequestProcessor;
    private Map<String, RequestProcessor> processors;

    public Dispatcher() {
        ItemsService itemsService = new ItemsService();
        this.processors = new HashMap<>();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultStaticRequestProcessor = new DefaultStaticResourceProcessor();
        this.processors.put("GET /calculate", new CalculatorRequestProcessor());
        this.processors.put("GET /hello", new HelloRequestProcessor());
        this.processors.put("GET /item", new GetItemsRequestProcessor(itemsService));
        this.processors.put("POST /item", new CreateItemsRequestProcessor(itemsService));
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (Files.exists(Paths.get("static/", request.getUri().substring(1)))) {
            defaultStaticRequestProcessor.execute(request, output);
            return;
        }
        RequestProcessor processor = processors.get(request.getRoutingUri());
        if (processor != null) {
           processor.execute(request, output);
        }
        throw new ResourceNotFoundException("resource not found " + request.getUri());
    }
}
