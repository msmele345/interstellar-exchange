package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.QuoteTest;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TradeGroupMapperTest extends QuoteTest {

    @InjectMocks
    private TradeGroupMapper tradeGroupMapper;

    @Test
    void mapQuotesToTradeGroups() {
        TradeGroup abcTradeGroup = TradeGroup.builder().symbol("ABC").quotePrices(asList(inputBid1, inputBid2, inputAsk)).build();
        TradeGroup bbnTradeGroup = TradeGroup.builder().symbol("BBN").quotePrices(asList(inputBid3, inputAsk2)).build();
        TradeGroup cazTradeGroup = TradeGroup.builder().symbol("CAZ").quotePrices(asList(inputBid4, inputAsk3)).build();
        TradeGroup ogcTradeGroup = TradeGroup.builder().symbol("OGC").quotePrices(asList(inputBid5, inputAsk4, inputAsk5)).build();

        List<TradeGroup> expectedGroupToBeSentToService = asList(abcTradeGroup, bbnTradeGroup, cazTradeGroup, ogcTradeGroup);

        List<QuotePrice> allQuotes = asList(inputBid1, inputBid2, inputBid3, inputBid4, inputBid5, inputAsk, inputAsk2, inputAsk3, inputAsk4, inputAsk5);

        List<TradeGroup> actual = tradeGroupMapper.mapQuotesToTradeGroups(allQuotes);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedGroupToBeSentToService);
    }
}