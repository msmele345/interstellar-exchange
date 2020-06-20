package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.*;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeMatcherService {

    private final TradeRepository tradeRepository;

    //feed result into this method to create trades and insert into trade repo.
    //they should all be the same symbol here from the orchestrator or realtime service
    public List<Trade> matchTrades(List<QuotePrice> quotes) {
        List<Trade> trades = new ArrayList<>();

        List<QuotePrice> bids = quotes.stream()
                .filter(q -> q.getClass().equals(Bid.class))
                .collect(Collectors.toList());

        List<QuotePrice> asks = quotes.stream()
                .filter(q -> q.getClass().equals(Ask.class))
                .collect(Collectors.toList());

        String symbol = quotes.get(0).getSymbol();

        String smallestSize = marketDepthCheck(bids, asks);

        long range = Long.min(bids.size(), asks.size());
        //if bids or asks are zero, range will be zero and no trades will be made.
        for (int i = 0; i < range; i++) {
            Integer bidId = bids.get(i).getId();
            Integer askId = asks.get(i).getId();
            Trade newTrade;

            double askPrice = asks.get(i).getPrice().doubleValue();
            double bidPrice = bids.get(i).getPrice().doubleValue();
            double priceDiff = askPrice - bidPrice;

            double threshold = askPrice * .005;

            boolean isTradeMatch = priceDiff <= threshold;

            if (isTradeMatch) {
                BigDecimal fillPrice = BigDecimal.valueOf(bidPrice + priceDiff / 2);

                Trade coolTrade = Trade.builder()
                        .bidId(bidId)
                        .askId(askId)
                        .symbol(symbol)
                        .tradePrice(fillPrice.setScale(2, RoundingMode.HALF_EVEN))
                        .build();
                //create trade at mid price
                trades.add(coolTrade);
            }
        }
        if (!trades.isEmpty()) {
            tradeRepository.saveAll(trades);
        }
        return trades;
    }

    public List<Trade> matchRealTimeTrades(List<TradeGroup> inboundGroups) {
        List<Trade> trades = new ArrayList<>();

        Map<String, List<TradeGroup>> groupsBySymbol = inboundGroups.stream()
                .collect(Collectors.groupingBy(TradeGroup::getSymbol));
        //change input to map from the orchestrator to prevent double group bys
        List<List<QuotePrice>> quotesForAllSymbols = new ArrayList<>(groupsBySymbol.values())
                .stream()
                .flatMap(e -> e.stream().map(TradeGroup::getQuotePrices))
                .collect(Collectors.toList());

        quotesForAllSymbols.forEach(quotesForSymbolList ->
                trades.addAll(matchTrades(quotesForSymbolList))
        );

        return trades;
    }

    protected String marketDepthCheck(List<QuotePrice> bids, List<QuotePrice> asks) {
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
