package ru.otus.java.basic.httpserver.model;

public class Book {
    private long id;
    private String author;
    private String title;

    public Book(long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public Book() {

    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
