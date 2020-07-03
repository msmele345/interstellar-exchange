package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.Trade;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeMatcherService {

    private final TradeRepository tradeRepository;
    private final TradeExecutionHelper tradeExecutionHelper;
    private final MarketCheckHelper marketCheckHelper;
    private final UpdateQuoteSystemService updateQuoteSystemService;

    public List<Trade> matchTrades(List<QuotePrice> quotes) {
        List<Trade> trades = null;

        List<QuotePrice> bids = quotes.stream()
                .filter(q -> q.getClass().equals(Bid.class))
                .collect(Collectors.toList());

        List<QuotePrice> asks = quotes.stream()
                .filter(q -> q.getClass().equals(Ask.class))
                .collect(Collectors.toList());

        String symbol = quotes.get(0).getSymbol();

        Map<QuotePrice, QuotePrice> tradeCandidates = marketCheckHelper.checkMarket(bids, asks);

        if (!tradeCandidates.entrySet().isEmpty()) {
            trades = tradeExecutionHelper.executeTrades(tradeCandidates, symbol);
        }

        if (nonNull(trades) && !trades.isEmpty()) {
            log.info("TRADE MADE FOR SYMBOL: " + trades.get(0).getSymbol() + " AT PRICE: " + trades.get(0).getTradePrice());
            //TODO find way to pass bid and ask of made trade to updateSystem
//            trades.forEach(trade -> updateQuoteSystemService.updateMarket(trade.getBidId(), trade.getAskId()));
            tradeRepository.saveAll(trades);
        }
        return trades;
    }
}
//takes each group of quote prices for symbol
//gets trade candidates by calling check market with the bids and asks for one symbol
//passes eligible candidates from map to helper to execute list of trades
//save trades to repo
