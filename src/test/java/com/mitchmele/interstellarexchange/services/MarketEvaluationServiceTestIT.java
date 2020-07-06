package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static java.util.Arrays.asList;

@SpringBootTest
class MarketEvaluationServiceTestIT {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AskRepository askRepository;

    @Autowired
    MarketEvaluationService marketEvaluationService;

    @BeforeEach
    void setUp() {
        Bid inputBid = Bid.builder().id(173).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(184).symbol("ABC").bidPrice(BigDecimal.valueOf(22.75)).build();
        Ask inputAsk = Ask.builder().id(156).symbol("ABC").askPrice(BigDecimal.valueOf(23.20)).build();
        Ask inputAsk2 = Ask.builder().id(190).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        bidRepository.saveAll(asList(inputBid, inputBid2));
        askRepository.saveAll(asList(inputAsk, inputAsk2));
    }

    @Test
    void evaluate() {
        //TODO
    }
}