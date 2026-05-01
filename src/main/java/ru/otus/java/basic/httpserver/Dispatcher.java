package ru.otus.java.basic.httpserver;

import ru.otus.java.basic.httpserver.Exeption.ResourceNotFoundException;
import ru.otus.java.basic.httpserver.db.DatabaseConnection;
import ru.otus.java.basic.httpserver.processors.*;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private DefaultStaticResourceProcessor defaultStaticRequestProcessor;
    private Map<String, RequestProcessor> processors;
    private DatabaseConnection dbConnection;

    public Dispatcher(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        BookRepository bookRepository = new BookRepository(dbConnection);

        this.processors = new HashMap<>();
        this.defaultStaticRequestProcessor = new DefaultStaticResourceProcessor();
        this.processors.put("GET /books", new GetBooksRequestProcessor(bookRepository));
        this.processors.put("GET /book", new FindBookRequestProcessor(bookRepository));
        this.processors.put("POST /book", new CreateBookRequestProcessor(bookRepository));
        this.processors.put("DELETE /book", new DeleteBookRequestProcessor(bookRepository));
        this.processors.put("PUT /book", new UpdateBookRequestProcessor(bookRepository));

    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (Files.exists(Paths.get("static/", request.getUri().substring(1)))) {
            defaultStaticRequestProcessor.execute(request, output);
            return;
        }

//        Map<String, String> processorsMethods = new HashMap<>();
//        processors.forEach((key, valiue) -> {
//            String[] methodUri = key.split(" ");
//            processorsMethods.put()
//        });

        RequestProcessor processor = processors.get(request.getRoutingUri());
        if (processor != null) {
            processor.execute(request, output);
            return;
        }
        throw new ResourceNotFoundException("resource not found " + request.getUri());
    }
}
