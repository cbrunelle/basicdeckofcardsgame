package com.demo.basicdeckofcards;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedOperationException extends RuntimeException {
    UnsupportedOperationException() {
        super("This operation is not currently supported.");
    }
}