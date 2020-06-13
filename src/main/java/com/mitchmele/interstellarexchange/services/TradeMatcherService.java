package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.Trade;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeMatcherService {

    private final BidRepository bidRepository;
    private final AskRepository askRepository;
    private final TradeRepository tradeRepository;


    public List<Trade> matchTrades() {
        return Collections.emptyList();
    }

}
