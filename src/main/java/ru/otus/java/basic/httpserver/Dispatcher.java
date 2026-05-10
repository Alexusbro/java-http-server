package ru.otus.java.basic.httpserver;

import ru.otus.java.basic.httpserver.exception.MethodNotAllowedException;
import ru.otus.java.basic.httpserver.exception.ResourceNotFoundException;
import ru.otus.java.basic.httpserver.db.DatabaseConnection;
import ru.otus.java.basic.httpserver.processors.*;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dispatcher {
    private final DefaultStaticResourceProcessor defaultStaticRequestProcessor;
    private final Map<String, RequestProcessor> processors;
    private final Map<String, Set<HttpMethod>> methodUriMap;

    public Dispatcher(DatabaseConnection dbConnection) {
        BookRepository bookRepository = new BookRepository(dbConnection);
        this.processors = new HashMap<>();
        methodUriMap = new HashMap<>();
        this.defaultStaticRequestProcessor = new DefaultStaticResourceProcessor();
        processorRegister(HttpMethod.GET, "/books", new GetBooksRequestProcessor(bookRepository));
        processorRegister(HttpMethod.GET, "/book", new FindBookRequestProcessor(bookRepository));
        processorRegister(HttpMethod.POST, "/book", new CreateBookRequestProcessor(bookRepository));
        processorRegister(HttpMethod.DELETE, "/book", new DeleteBookRequestProcessor(bookRepository));
        processorRegister(HttpMethod.PUT, "/book", new UpdateBookRequestProcessor(bookRepository));
    }

    private void processorRegister(HttpMethod method, String uri, RequestProcessor processor) {
        this.processors.put(method + " " + uri, processor);
        this.methodUriMap.computeIfAbsent(uri, k -> new HashSet<>()).add(method);
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (Files.exists(Paths.get("static/", request.getUri().substring(1)))) {
            
            defaultStaticRequestProcessor.execute(request, output);
            return;
        }

        RequestProcessor processor = processors.get(request.getRoutingUri());
        if (processor != null) {
            processor.execute(request, output);
            return;
        }

        if (methodUriMap.containsKey(request.getUri())) {
            throw new MethodNotAllowedException("method not allowed");
        }

        throw new ResourceNotFoundException("resource not found " + request.getUri());
    }
}
