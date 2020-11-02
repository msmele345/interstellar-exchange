package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.*;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("IT")
class UpdateQuoteSystemServiceTestIT {

    @Autowired
    private UpdateQuoteSystemService updateQuoteSystemService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AskRepository askRepository;

    @Test
    void updateQuotes_removesBidAndAsksUsedInTradesFromRepos() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.75)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.20)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        List<QuotePrice> allQuotes = asList(inputBid, inputBid2, inputAsk, inputAsk2);

        Trade trade = Trade.builder()
                .bidId(73)
                .askId(90)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.03))
                .build();

        List<Trade> actualTrades = asList(trade);

        bidRepository.saveAll(asList(inputBid, inputBid2));
        askRepository.saveAll(asList(inputAsk, inputAsk2));

        updateQuoteSystemService.updateMarket(allQuotes, actualTrades);

        assertThat(bidRepository.findAllBySymbol("ABC")).doesNotContain(inputBid);
        assertThat(askRepository.findAllBySymbol("ABC")).doesNotContain(inputAsk2);
    }
}