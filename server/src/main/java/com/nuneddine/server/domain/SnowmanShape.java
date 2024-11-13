package com.nuneddine.server.domain;

public enum SnowmanShape {
    BASIC("basic"),
    TRIPLE("triple"),
    PUANG("puang"),
    LION("lion");

    private final String shape;

    SnowmanShape(String shape) {
        this.shape = shape;
    }
}