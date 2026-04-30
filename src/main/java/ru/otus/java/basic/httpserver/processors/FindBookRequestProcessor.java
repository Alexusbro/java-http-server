package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.model.Book;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class FindBookRequestProcessor implements RequestProcessor {
    private BookRepository bookRepository;

    public FindBookRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
    String idParam = request.getParameters("id");
    long id = Long.parseLong(idParam);
        HttpResponse response = new HttpResponse(output);
        Gson gson = new Gson();
        String result = gson.toJson(bookRepository.getBookById(id), Book.class);
        response.setBody(result);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}
