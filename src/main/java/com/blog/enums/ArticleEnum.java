package com.blog.enums;

public enum ArticleEnum {
    OOP("OOP"),
    JAVA("JAVA"),
    DEV_OPS("DEV_OPS"),
    JS("JS");

    private final String description;

    ArticleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
