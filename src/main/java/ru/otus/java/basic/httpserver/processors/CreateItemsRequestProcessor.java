package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.HttpStatus;
import ru.otus.java.basic.httpserver.app.Item;
import ru.otus.java.basic.httpserver.app.ItemsService;

import java.io.IOException;
import java.io.OutputStream;


public class CreateItemsRequestProcessor implements RequestProcessor {
    private ItemsService itemsService;

    public CreateItemsRequestProcessor(ItemsService itemsService) {
        this.itemsService = itemsService;
    }
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(request.getBody(), Item.class);
        itemsService.addItem(item);

        HttpResponse response = new HttpResponse(output);
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}

