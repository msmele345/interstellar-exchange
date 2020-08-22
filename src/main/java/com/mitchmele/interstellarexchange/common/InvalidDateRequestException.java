package com.mitchmele.interstellarexchange.common;


public class InvalidDateRequestException extends RuntimeException {
    public InvalidDateRequestException(String message) {
        super(message);
    }
}
