package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.QuoteTest;
import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RealtimeMatcherServiceTest extends QuoteTest {

    @Mock
    private TradeMatcherService tradeMatcherService;

    @InjectMocks
    private RealtimeMatcherService realtimeMatcherService;

    public Bid inputBid1 = Bid.builder().id(162).symbol("ABC").bidPrice(BigDecimal.valueOf(23.05)).build();
    public Bid inputBid2 = Bid.builder().id(163).symbol("ABC").bidPrice(BigDecimal.valueOf(23.07)).build();
    public Bid inputBid3 = Bid.builder().id(164).symbol("BBN").bidPrice(BigDecimal.valueOf(13.25)).build();
    public Bid inputBid4 = Bid.builder().id(172).symbol("CAZ").bidPrice(BigDecimal.valueOf(3.06)).build();
    public Bid inputBid5 = Bid.builder().id(22).symbol("OGC").bidPrice(BigDecimal.valueOf(113.03)).build();

    public Ask inputAsk = Ask.builder().id(19).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();
    public Ask inputAsk2 = Ask.builder().id(20).symbol("BBN").askPrice(BigDecimal.valueOf(13.25)).build();
    public Ask inputAsk3 = Ask.builder().id(21).symbol("CAZ").askPrice(BigDecimal.valueOf(3.07)).build();
    public Ask inputAsk4 = Ask.builder().id(22).symbol("OGC").askPrice(BigDecimal.valueOf(113.03)).build();
    public Ask inputAsk5 = Ask.builder().id(23).symbol("OGC").askPrice(BigDecimal.valueOf(113.08)).build();

    @Test
    void matchRealTimeTrades_organizesQuotesForEachSymbolAndCallsService() {

        TradeGroup abcTradeGroup = TradeGroup.builder().symbol("ABC").quotePrices(asList(inputBid1, inputBid2, inputAsk)).build();
        TradeGroup bbnTradeGroup = TradeGroup.builder().symbol("BBN").quotePrices(asList(inputBid3, inputAsk2)).build();
        TradeGroup cazTradeGroup = TradeGroup.builder().symbol("CAZ").quotePrices(asList(inputBid4, inputAsk3)).build();
        TradeGroup ogcTradeGroup = TradeGroup.builder().symbol("OGC").quotePrices(asList(inputBid5, inputAsk4, inputAsk5)).build();

        List<TradeGroup> inboundGroups = asList(abcTradeGroup, bbnTradeGroup, cazTradeGroup, ogcTradeGroup);

        realtimeMatcherService.matchRealTimeTrades(inboundGroups);

        verify(tradeMatcherService).matchTrades(asList(inputBid1, inputBid2, inputAsk));
        verify(tradeMatcherService).matchTrades(asList(inputBid3, inputAsk2));
        verify(tradeMatcherService).matchTrades(asList(inputBid4, inputAsk3));
        verify(tradeMatcherService).matchTrades(asList(inputBid5, inputAsk4, inputAsk5));
    }
}