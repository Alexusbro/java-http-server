package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
import ru.otus.java.basic.httpserver.model.Book;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class CreateBookRequestProcessor implements RequestProcessor{
    private BookRepository bookRepository;

    public CreateBookRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Book book = gson.fromJson(request.getBody(), Book.class);
        bookRepository.saveBook(book);
        HttpResponse response = new HttpResponse(output);
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}
