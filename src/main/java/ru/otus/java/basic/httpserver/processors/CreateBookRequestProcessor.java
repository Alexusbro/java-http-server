package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
import ru.otus.java.basic.httpserver.model.Book;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class CreateBookRequestProcessor implements RequestProcessor {
    private final BookRepository bookRepository;

    public CreateBookRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(output);
        if (request.getBody() == null || request.getBody().isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("request body is required");
            response.send();
            return;
        }
        Gson gson = new Gson();
        Book book = gson.fromJson(request.getBody(), Book.class);
        bookRepository.saveBook(book);

        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}
