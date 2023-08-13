package com.blog.enums;

public enum RoleEnum {
    USER("User"),
    ADMIN("Admin");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
