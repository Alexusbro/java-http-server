package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
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
        HttpResponse response = new HttpResponse(output);
        if (idParam == null || idParam.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("empty id");
            response.send();
            return;
        }
        long id = 0;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("invalid format id");
            response.send();
            return;
        }
        Gson gson = new Gson();
        Book book = bookRepository.getBookById(id);
        if (book == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setBody("not found book with id " + id);
            response.send();
            return;
        }
        String result = gson.toJson(book);
        response.setBody(result);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}
