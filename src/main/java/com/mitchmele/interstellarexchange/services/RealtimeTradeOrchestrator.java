package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeTradeOrchestrator {

    private final RealtimeMatcherService realtimeMatcherService;

    //listener passes here
    public void processRealTimeQuotes(List<QuotePrice> inboundQuotes) {
        //organizes by symbol and prices (tradeGroups)
        //calls matchRealTimeTrades in tradeMatcherService with quotes from jms
        List<TradeGroup> tradeGroups = inboundQuotes.stream()
                .collect(Collectors.groupingBy(QuotePrice::getSymbol))
                .entrySet().stream()
                .map(quote ->
                        TradeGroup.builder()
                                .symbol(quote.getKey())
                                .quotePrices(quote.getValue())
                                .build()
                ).collect(Collectors.toList());

        realtimeMatcherService.matchRealTimeTrades(tradeGroups);
    }
}
