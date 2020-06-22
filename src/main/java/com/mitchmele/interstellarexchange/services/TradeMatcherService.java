package com.mitchmele.interstellarexchange.services;
import com.mitchmele.interstellarexchange.model.*;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeMatcherService {

    private final TradeRepository tradeRepository;
    private final TradeExecutionHelper tradeExecutionHelper;

    //takes each group of quote prices for symbol
    //gets trade candidates by calling check market with the bids and asks for one symbol
    //passes eligible candidates from map to helper to execute list of trades
    //save trades to repo
    public List<Trade> matchTrades(List<QuotePrice> quotes) {
        List<QuotePrice> bids = quotes.stream()
                .filter(q -> q.getClass().equals(Bid.class))
                .collect(Collectors.toList());

        List<QuotePrice> asks = quotes.stream()
                .filter(q -> q.getClass().equals(Ask.class))
                .collect(Collectors.toList());

        String symbol = quotes.get(0).getSymbol();

        Map<QuotePrice, QuotePrice> tradeCandidates = checkMarket(bids, asks);

        List<Trade> trades = tradeExecutionHelper.executeTrades(tradeCandidates, symbol);

        if (!trades.isEmpty()) {
            tradeRepository.saveAll(trades);
        }
        return trades;
    }

    //takes trade groups for different symbols with different bids and offers for each symbol
    //each group has a symbol and a list of prices - may or may not be trade eligible
    //breaks up into a list of bids+ offers for symbol and sends to matcher
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

    public Map<QuotePrice, QuotePrice> checkMarket(List<QuotePrice> bids, List<QuotePrice> asks) {
        //feed this into matchTrades
        //OR feed into new service/method that takes a map param and creates trades then saves to the repo
        Map<QuotePrice, QuotePrice> result = new HashMap<>();
        asks.forEach(ask -> {
            double askPrice = ask.getPrice().doubleValue();
            double threshold = askPrice - (askPrice * .005);
            for (QuotePrice bid : bids) {
                double bidPrice = bid.getPrice().doubleValue();
                if (bidPrice >= threshold && !result.containsValue(ask) && !result.containsKey(bid)) {
                    result.put(bid, ask);
                }
            }
        });
        return result;
    }
}
