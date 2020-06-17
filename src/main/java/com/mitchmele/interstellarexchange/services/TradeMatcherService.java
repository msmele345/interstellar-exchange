package com.mitchmele.interstellarexchange.services;

import com.google.common.annotations.VisibleForTesting;
import com.mitchmele.interstellarexchange.model.*;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeMatcherService {

    private final TradeRepository tradeRepository;

    //feed result into this method to create trades and insert into trade repo.
    //they should all be the same symbol here from the orchestrator
    public List<Trade> matchTrades(List<QuotePrice> quotes) {
        String smallestSize = null;
        List<Trade> trades = new ArrayList<>();

        /*
         * Need logic that checks if the prices are not the same.
         * Check for how close the bid/ask is and make trade on ask if it works
         * */

        List<QuotePrice> bids = quotes.stream()
                .filter(q -> q.getClass().equals(Bid.class))
                .collect(Collectors.toList());

        List<QuotePrice> asks = quotes.stream()
                .filter(q -> q.getClass().equals(Ask.class))
                .collect(Collectors.toList());

        String symbol = quotes.get(0).getSymbol();

        if (bids.size() <= asks.size()) {
            smallestSize = "BID";
        }
        if (asks.size() < bids.size()) {
            smallestSize = "ASK";
        }
        long range = Long.min(bids.size(), asks.size());

        for (int i = 0; i < range; i++) {
            Integer bidId = bids.get(i).getId();
            Integer askId = asks.get(i).getId();
            Trade newTrade;
            if ("BID".equals(smallestSize)) {
                newTrade = Trade.builder()
                        .symbol(symbol)
                        .bidId(bidId)
                        .askId(askId)
                        .tradePrice(bids.get(i).getPrice())
                        .build();
            } else {
                newTrade = Trade.builder()
                        .symbol(symbol)
                        .bidId(bidId)
                        .askId(askId)
                        .tradePrice(asks.get(i).getPrice())
                        .build();
            }
            trades.add(newTrade);
        }
        tradeRepository.saveAll(trades);
        return trades;
    }

    public List<Trade> matchRealTimeTrades(List<TradeGroup> inboundGroups) {
        Map<String, List<TradeGroup>> groupsBySymbol = inboundGroups.stream()
                .collect(Collectors.groupingBy(TradeGroup::getSymbol));
        //change input to map from the orchestrator to prevent double group bys
        List<List<QuotePrice>> quotes = groupsBySymbol.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())
                .stream()
                .flatMap(e -> e.stream().map(TradeGroup::getQuotePrices))
                .collect(Collectors.toList());

        List<Trade> trades = new ArrayList<>();

        quotes.forEach(quotesForSymbol -> trades.addAll(matchTrades(quotesForSymbol)));

        return trades;
    }


    protected String marketDepthCheck(List<Bid> bids, List<Ask> asks) {
        String smallestDepth = null;
        if (bids.size() == 0 && asks.size() == 0) return "ZERO";

        if (bids.size() <= asks.size()) {
            smallestDepth = "BID";
        }
        if (asks.size() < bids.size()) {
            smallestDepth = "ASK";
        }
        return smallestDepth;
    }
}
