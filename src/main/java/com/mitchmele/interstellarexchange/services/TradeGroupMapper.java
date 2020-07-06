package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradeGroupMapper {

    public List<TradeGroup> mapQuotesToTradeGroups(List<QuotePrice> quotes) {
        return quotes.stream()
                .collect(Collectors.groupingBy(QuotePrice::getSymbol))
                .entrySet().stream()
                .map(quote ->
                        TradeGroup.builder()
                                .symbol(quote.getKey())
                                .quotePrices(quote.getValue())
                                .build()
                ).collect(Collectors.toList());
    }
}
