package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuotePrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MarketCheckHelperTest {

    @InjectMocks
    private MarketCheckHelper marketCheckHelper;

    //happy path one match mixed order
    @Test
    void checkMarket_returnsMapContainingBidAndAskForSymbol_thatMeetsTradeCriteria() {

        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.75)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.20)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        Map<QuotePrice, QuotePrice> expected = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk2);
        }};
        Map<QuotePrice, QuotePrice> actual = marketCheckHelper.checkMarket(asList(inputBid, inputBid2), asList(inputAsk, inputAsk2));
        assertThat(actual).isEqualTo(expected);
    }

    //happy path multiple matches
    @Test
    public void checkMarket_returnsMapOfMultipleBidAndAskThatMeetsThresholdCriteria() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.99)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();
        Ask inputAsk3 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.07)).build();

        Map<QuotePrice, QuotePrice> expected = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk2);
            put(inputBid2, inputAsk);
        }};

        Map<QuotePrice, QuotePrice> actual = marketCheckHelper.checkMarket(asList(inputBid, inputBid2), asList(inputAsk, inputAsk2, inputAsk3));
        assertThat(actual.entrySet()).hasSize(2);
        assertThat(actual).containsKeys(inputBid, inputBid2);
        assertThat(actual).containsValues(inputAsk, inputAsk2);
    }

    //happy path odd sizes
    @Test
    public void checkMarket_returnsMapOfMultipleBidAndAsk_withOddSizes() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.51)).build();

        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();

        Map<QuotePrice, QuotePrice> actual = marketCheckHelper.checkMarket(asList(inputBid, inputBid2), asList(inputAsk));
        assertThat(actual).containsKeys(inputBid);
        assertThat(actual).containsValues(inputAsk);
    }

    //empty map no matches
    @Test
    public void checkMarket_returnsEmptyMap_ifNoPricesMeetCriteriaForTrade() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(22.25)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.51)).build();

        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();
        Ask inputAsk2 = Ask.builder().id(58).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        Map<QuotePrice, QuotePrice> actual = marketCheckHelper.checkMarket(asList(inputBid, inputBid2), asList(inputAsk, inputAsk2));
        assertThat(actual).isEmpty();
    }
}