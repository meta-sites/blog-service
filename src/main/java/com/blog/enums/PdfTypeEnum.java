package com.blog.enums;

public enum PdfTypeEnum {
    CV("CV"),
    BOOK("BOOK");

    private final String description;

    PdfTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
