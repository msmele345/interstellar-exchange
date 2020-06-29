package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.Trade;
import com.mitchmele.interstellarexchange.model.TradeGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeMatcherService {

    private final TradeMatcherService tradeMatcherService;

    public void matchRealTimeTrades(List<TradeGroup> inboundGroups) {
        Map<String, List<TradeGroup>> groupsBySymbol = inboundGroups.stream()
                .collect(Collectors.groupingBy(TradeGroup::getSymbol));

        List<List<QuotePrice>> quotesForAllSymbols = new ArrayList<>(groupsBySymbol.values())
                .stream()
                .flatMap(e -> e.stream().map(TradeGroup::getQuotePrices))
                .collect(Collectors.toList());

        quotesForAllSymbols.forEach(quotesForSymbolList ->
                tradeMatcherService.matchTrades(quotesForSymbolList)
        );
    }
}
//takes trade groups for different symbols with different bids and offers for each symbol
//each group has a symbol and a list of prices - may or may not be trade eligible
//breaks up into a list of bids+ offers for symbol and sends to matcher
