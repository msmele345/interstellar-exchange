package com.mitchmele.interstellarexchange.quote;

import java.math.BigDecimal;

public interface QuotePrice {
    Integer getId();
    String getSymbol();
    BigDecimal getPrice();
}
