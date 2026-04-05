package ru.otus.java.basic.httpserver.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.httpserver.HttpRequest;
import ru.otus.java.basic.httpserver.app.Item;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetItemsRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        List<Item> items = new ArrayList<>(Arrays.asList(
                new Item(1L, "Bread", 50),
                new Item(2L, "Milk", 150),
                new Item(1L, "Cheese", 400)
                        ));
        Gson gson = new Gson();
        String result = gson.toJson(items);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/gson\r\n" +
                "\r\n" +
                result;

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}

