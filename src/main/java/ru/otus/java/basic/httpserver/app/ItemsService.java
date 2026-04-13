package ru.otus.java.basic.httpserver.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemsService {
   private List<Item> items;

    public ItemsService() {
        this.items = new ArrayList<>(Arrays.asList(
                new Item(1L, "Bread", 50),
                new Item(2L, "Milk", 150),
                new Item(3L, "Cheese", 400)
        ));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Item item) {
        Long newId = items.stream().mapToLong(Item::getId).max().orElse(0L) + 1L;
        item.setId(newId);
        items.add(item);
    }

}
