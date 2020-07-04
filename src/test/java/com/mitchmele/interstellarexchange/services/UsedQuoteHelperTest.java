package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UsedQuoteHelperTest {

    @InjectMocks
    private UsedQuoteHelper usedQuoteHelper;

    @Test
    void fetchUsedQuotes() {
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

        List<QuotePrice> expected = asList(inputBid, inputAsk2);
        List<QuotePrice> actual = usedQuoteHelper.fetchUsedQuotes(allQuotes, actualTrades);
        assertThat(actual).isEqualTo(expected);
    }
}