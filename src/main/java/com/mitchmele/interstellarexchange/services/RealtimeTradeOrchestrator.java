package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.TradeGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeTradeOrchestrator {

    private final TradeMatcherService tradeMatcherService;
    private final QuotePreProcessorService quotePreProcessorService;

    //manual or scheduled job
    public void orchestrate(String symbol) {
        //use matcher service to match trades
        //obtains current quotes from quotePreProcessorService and passes to trade service
        List<QuotePrice> quotesForSymbol = quotePreProcessorService.fetchQuotesForSymbol(symbol);
        tradeMatcherService.matchTrades(quotesForSymbol);
    }

    //listener passes here
    public void processRealTimeQuotes(List<QuotePrice> inboundQuotes) {
        //organizes by symbol and price
        //calls trade matcher service with quotes from jms
        Map<String, List<QuotePrice>> quotesForSymbol = inboundQuotes
                .stream()
                .collect(Collectors.groupingBy(QuotePrice::getSymbol));

        List<TradeGroup> tradeGroups = quotesForSymbol.entrySet().stream()
                .map(symbol ->
                        TradeGroup.builder()
                                .symbol(symbol.getKey())
                                .quotePrices(symbol.getValue()) //list of quotes
                                .build()
                ).collect(Collectors.toList());

        tradeMatcherService.matchRealTimeTrades(tradeGroups);
    }
}
