package com.nuneddine.server.domain;

public enum ItemCategory {
    SHAPE("Shape"),
    COLOR("Color"),
    EYE("Eye"),
    NOSE("Nose"),
    MOUTH("Mouth"),
    HAT("Hat"),
    MUFFLER("Muffler"),
    COAT("Coat"),
    SHIRT("Shirt"); // custom은 여기에

    private final String label;

    ItemCategory(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
