package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradeExecutionHelperTest {

    @InjectMocks
    private TradeExecutionHelper tradeExecutionHelper;

    @Test
    public void executeTrades_returnsListOfTradesOfQualifiedInputMapCandidates() {
        Bid inputBid = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(23.04)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.08)).build();

        Bid inputBid2 = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.25)).build();
        Ask inputAsk2 = Ask.builder().id(14).symbol("ABC").askPrice(BigDecimal.valueOf(23.30)).build();

        Map<QuotePrice, QuotePrice> inputMap = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk);
            put(inputBid2, inputAsk2);
        }};

        Trade expectedTrade = Trade.builder()
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.06))
                .build();

        Trade expectedTrade2 = Trade.builder()
                .bidId(73)
                .askId(14)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.275))
                .build();

        List<Trade> expected = asList(expectedTrade, expectedTrade2);

        List<Trade> actual = tradeExecutionHelper.executeTrades(inputMap, "ABC");

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void executeTrades_returnsEmptyListIfBidAndOffersMapIsEmpty() {
        Map<QuotePrice, QuotePrice> incomingMap = new HashMap<>();

        List<Trade> actual = tradeExecutionHelper.executeTrades(incomingMap, "ABC");
        assertThat(actual).isEmpty();
    }
}