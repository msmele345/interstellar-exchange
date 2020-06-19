package com.mitchmele.interstellarexchange.common;

public enum ErrorType {
    ROUTING("ROUTING"),
    PROCESSING("PROCESSING"),
    TRADE_ERROR("TRADE ERROR"),
    UNKNOWN("UNKNOWN");

    public final String value;

    ErrorType(String value) {
        this.value = value;
    }
}
