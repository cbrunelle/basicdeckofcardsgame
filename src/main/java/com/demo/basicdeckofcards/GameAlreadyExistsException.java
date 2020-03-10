
package com.demo.basicdeckofcards;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameAlreadyExistsException extends RuntimeException {
    GameAlreadyExistsException() {
        super("The name provided already matches an existing game.");
    }
}