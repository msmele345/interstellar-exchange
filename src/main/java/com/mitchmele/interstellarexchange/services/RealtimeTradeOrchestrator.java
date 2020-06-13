package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeTradeOrchestrator {

    private final TradeMatcherService tradeMatcherService;

    public void orchestrate(List<QuotePrice> quotes) {
        //use matcher service to match trades
    }
}
