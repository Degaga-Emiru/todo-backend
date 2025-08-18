package com.example.todoapplication.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}