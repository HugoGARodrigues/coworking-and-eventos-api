package com.example.coworking_and_eventos_api.exceptions;


public class InvalidStatusChangeException extends RuntimeException {
    public InvalidStatusChangeException(String message) {
        super(message);
    }
}
