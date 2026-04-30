package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
import ru.otus.java.basic.httpserver.model.Book;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class UpdateBookRequestProcessor implements RequestProcessor {
    private BookRepository bookRepository;

    public UpdateBookRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(output);
        Gson gson = new Gson();
        try{
            Book book = gson.fromJson(request.getBody(), Book.class);
            if (book.getId() == 0L) {
                response.setBody("uncorrect id in JSON body");
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.send();
                return;
            }
            bookRepository.updateBookById(book.getId(), book);
            response.setBody("book updated successfully");
            response.send();

        }catch (JsonSyntaxException e) {
            response.setBody("invalid JSON format " + e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.send();
        }

    }
}
