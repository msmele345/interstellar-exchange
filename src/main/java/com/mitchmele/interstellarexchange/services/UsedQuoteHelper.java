package com.mitchmele.interstellarexchange.services;


import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsedQuoteHelper {

    public List<QuotePrice> fetchUsedQuotes(List<QuotePrice> allQuotes, List<Trade> actualTrades) {
        List<Integer> bidIds = actualTrades.stream()
                .map(Trade::getBidId)
                .collect(Collectors.toList());

        List<Integer> askIds =  actualTrades.stream()
                .map(Trade::getAskId)
                .collect(Collectors.toList());

        return allQuotes.stream()
                .filter(quote -> bidIds.contains(quote.getId()) || askIds.contains(quote.getId()))
                .collect(Collectors.toList());
    }
}
