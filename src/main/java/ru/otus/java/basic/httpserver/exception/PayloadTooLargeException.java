package ru.otus.java.basic.httpserver.exception;

import java.io.IOException;

public class PayloadTooLargeException extends IOException {
    public PayloadTooLargeException(String message) {
        super(message);
    }
}
