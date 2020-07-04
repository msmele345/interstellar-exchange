package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotePreProcessorService {

    private final BidRepository bidRepository;
    private final AskRepository askRepository;

    public List<QuotePrice> fetchQuotesForSymbol(String symbol) {
        List<QuotePrice> liveQuotesForSymbol = new ArrayList<>();

        List<Bid> bids = bidRepository.findAllBySymbol(symbol);
        List<Ask> asks = askRepository.findAllBySymbol(symbol);
        liveQuotesForSymbol.addAll(bids);
        liveQuotesForSymbol.addAll(asks);

        return liveQuotesForSymbol;
    }

}
