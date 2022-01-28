package com.creditshelf.assignment.exception;

public class InvalidMoveException extends Exception {

    private String message;

    public InvalidMoveException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
