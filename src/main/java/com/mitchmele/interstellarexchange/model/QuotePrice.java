package com.mitchmele.interstellarexchange.model;

import java.math.BigDecimal;

public interface QuotePrice {
    Integer getId();
    String getSymbol();
    BigDecimal getPrice();
}
