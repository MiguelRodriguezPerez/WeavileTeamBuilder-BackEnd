package com.example.demo.domain.team;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TeamType {
    INDIVIDUAL(0),
    DOUBLE(1),
    VGC(2),
    ONEVSONE(3),
    MONOTYPE(4);

    private final int value;

    TeamType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static TeamType fromValue(int value) {
        for (TeamType type : TeamType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TeamType value: " + value);
    }
}
