package com.demo.basicdeckofcards;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerNotFoundException extends RuntimeException {
    PlayerNotFoundException() {
        super("Player is not in the game.");
    }
}