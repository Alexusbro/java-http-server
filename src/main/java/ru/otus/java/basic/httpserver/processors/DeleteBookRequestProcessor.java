package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
import ru.otus.java.basic.httpserver.repository.BookRepository;

import java.io.IOException;
import java.io.OutputStream;

public class DeleteBookRequestProcessor implements RequestProcessor {
    private BookRepository bookRepository;

    public DeleteBookRequestProcessor(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse(output);
        String idParam = request.getParameters("id");
        if (idParam == null || idParam.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("Empty id parameter");
            response.send();
            return;
        }
        try {
            long id = Long.parseLong(idParam);
            bookRepository.deleteBookById(id);
            response.setBody("Book deleted successfully");
            response.send();
        } catch (NumberFormatException e) {
            response.setBody("uncorrected id format");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.send();
        }
    }
}
