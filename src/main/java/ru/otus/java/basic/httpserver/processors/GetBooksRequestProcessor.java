package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class GetBooksRequestProcessor implements RequestProcessor{
    private final BookRepository bookRepository;

    public GetBooksRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        String result = gson.toJson(bookRepository.findAllBook());
        HttpResponse response = new HttpResponse(output);
        response.setBody(result);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}
