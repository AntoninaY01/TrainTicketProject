package com.example.app.entities.enums;

public enum PassengerType {
    FAMILY("FAMILY"),
    OVER_60("OVER_60"),
    NONE("NONE");

    private final String text;

    PassengerType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
