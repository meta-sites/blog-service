package com.blog.enums;

public enum UserTypeEnum {
    NORMAL("NORMAL"),
    GOOGLE("GOOGLE"),
    FACEBOOK("FACEBOOK");

    private final String description;

    UserTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
