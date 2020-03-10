package com.demo.basicdeckofcards;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
    GameNotFoundException() {
        super("The name provided does not match any existing game.");
    }
}
