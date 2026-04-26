package ru.otus.java.basic.httpserver.Exeption;

import java.io.IOException;

public class PayloadTooLargeException extends IOException {
    public PayloadTooLargeException(String message) {
        super(message);
    }
}
