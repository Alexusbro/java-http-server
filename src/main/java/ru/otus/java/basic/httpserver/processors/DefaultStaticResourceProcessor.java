package ru.otus.java.basic.httpserver.processors;

import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultStaticResourceProcessor implements RequestProcessor{

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String filename = request.getUri().substring(1);
        Path filePath = Paths.get("static/", filename);
        byte[] fileData = Files.readAllBytes(filePath);
        String typeFile = Files.probeContentType(filePath);

        HttpResponse response = new HttpResponse(output);
        response.setBody(fileData, typeFile);
        response.send();

    }
}
