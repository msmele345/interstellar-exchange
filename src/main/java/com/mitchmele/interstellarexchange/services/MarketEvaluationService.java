package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketEvaluationService {

    private final BidRepository bidRepository;

    private final AskRepository askRepository;

    public List<QuotePrice> evaluate() {
        List<QuotePrice> allQuotes = new ArrayList<>();

        allQuotes.addAll(bidRepository.findAll());
        allQuotes.addAll(askRepository.findAll());
        return allQuotes;
    }

    public List<QuotePrice> evaluateForSymbol(String symbol) {
        List<QuotePrice> liveQuotesForSymbol = new ArrayList<>();

        List<Bid> bids = bidRepository.findAllBySymbol(symbol);
        List<Ask> asks = askRepository.findAllBySymbol(symbol);
        liveQuotesForSymbol.addAll(bids);
        liveQuotesForSymbol.addAll(asks);

        return liveQuotesForSymbol;
    }
}
