package com.demo.basicdeckofcards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupByModes {
    NONE("none"),
    SUIT("suit"),
    CARD("card");

    private String key;

    GroupByModes(String key) {
        this.key = key;
    }

    @JsonCreator
    public static GroupByModes fromString(String key) {
        return key == null
                ? null
                : GroupByModes.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}