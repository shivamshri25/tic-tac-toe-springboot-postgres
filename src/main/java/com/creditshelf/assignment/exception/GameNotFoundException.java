package com.creditshelf.assignment.exception;

import java.util.function.Supplier;

public class GameNotFoundException extends Exception {

    private String message;

    public GameNotFoundException(String game_not_found) {
        this.message = game_not_found;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
