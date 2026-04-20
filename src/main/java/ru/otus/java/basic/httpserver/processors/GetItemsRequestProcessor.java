package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.HttpResponse;
import ru.otus.java.basic.httpserver.app.ItemsService;

import java.io.IOException;
import java.io.OutputStream;


public class GetItemsRequestProcessor implements RequestProcessor {
    private ItemsService itemsService;

    public GetItemsRequestProcessor(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        String result = gson.toJson(itemsService.getItems());
        HttpResponse response = new HttpResponse(output);
        response.setBody(result);
        response.setContentType(HttpResponse.CONTENT_JSON);
        response.send();
    }
}

