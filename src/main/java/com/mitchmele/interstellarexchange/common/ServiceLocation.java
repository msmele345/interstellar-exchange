package com.mitchmele.interstellarexchange.common;

public enum ServiceLocation {
    JMS("JMS"),
    TRADE_SERVICES("TRADE SERVICES"),
    DATABASE("DATABASE"),
    UNKNOWN("UNKNOWN");

    public final String value;

     ServiceLocation(String value) {
        this.value = value;
    }
}
