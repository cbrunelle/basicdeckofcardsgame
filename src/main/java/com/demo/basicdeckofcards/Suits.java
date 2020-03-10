package com.demo.basicdeckofcards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Suits {
    HEARTS ("hearts", 0),
    SPADES ("spades", 1),
    CLUBS ("clubs", 2),
    DIAMONDS ("diamonds", 3);

    private final Object[] values;

    private Suits(Object... vals) {
        values = vals;
    }

    public String Name() {
        return (String) values[0];
    }

    public String Value() {
        return (String) values[1];
    }

    public String toString() {
        return this.Name();
    }

    @JsonCreator
    public static Suits fromString(String key) {
        return key == null
                ? null
                : Suits.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return this.Name();
    }
}