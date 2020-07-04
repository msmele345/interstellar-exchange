package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.Trade;
import com.mitchmele.interstellarexchange.trade.repository.TradeRepository;
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
            log.info(trades.size() +  " TRADE(S) MADE FOR SYMBOL: " + trades.get(0).getSymbol());
            tradeRepository.saveAll(trades);
            updateQuoteSystemService.updateMarket(quotes, trades);
        }
        return trades;
    }
}
