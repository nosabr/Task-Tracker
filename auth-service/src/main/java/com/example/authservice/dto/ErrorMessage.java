package com.example.authservice.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessage {
    USER_ALREADY_EXISTS("This username is already taken"),
    WRONG_LOGIN_OR_PASSWORD("Invalid username or password"),
    SYSTEM_ERROR("An error occurred. Please try again later"),
    INVALID_FORMAT("Invalid username or password format"),
    UNAUTHORIZED("Unauthorized"),
    USER_NOT_FOUND("User not found"),
    BAD_REQUEST("Bad request");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @JsonValue
    public String getMessage() {
        return message;
    }
}
