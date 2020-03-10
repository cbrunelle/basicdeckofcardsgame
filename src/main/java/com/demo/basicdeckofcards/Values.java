package com.demo.basicdeckofcards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Values {
    KING("King", 13),
    QUEEN("Queen", 12),
    JACK("Jack", 11),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2),
    ACE("Ace", 1);

    private final Object[] values;

    private Values(Object... vals) {
        values = vals;
    }

    public String Name() {
        return (String) values[0];
    }

    public Integer Value() {
        return (Integer) values[1];
    }

    public String toString() {
        return this.Name();
    }

    @JsonCreator
    public static Values fromString(String key) {
        return key == null
                ? null
                : Values.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return this.Name();
    }
}
