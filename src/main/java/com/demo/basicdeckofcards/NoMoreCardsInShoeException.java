
package com.demo.basicdeckofcards;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoMoreCardsInShoeException extends RuntimeException {
    NoMoreCardsInShoeException() {
        super("There is no more cards to be dealt in shoe.");
    }
}