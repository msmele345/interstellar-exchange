package com.mitchmele.interstellarexchange.trade;

import com.mitchmele.interstellarexchange.quote.QuotePrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TradeGroup {
    private String symbol;
    private List<QuotePrice> quotePrices;
}
