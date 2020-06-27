package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.QuoteTest;
import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.Trade;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeMatcherServiceTest extends QuoteTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private MarketCheckHelper marketCheckHelper;

    @Mock
    private TradeExecutionHelper tradeExecutionHelper;

    @InjectMocks
    private TradeMatcherService tradeMatcherService;


    //matchRealtimeTrades:
    //breaks tradeGroups up by symbol
    //grabs quote prices for a symbol (list)
    //sends list of quotes to match trades
    //match trades checks size of bids and asks and creates trades
    //trades are saved

    //FILL ALGORITHM:
    //diff .005
    //ask - bid == diff
    //if diff is within .005 bounds, then fill trade at mid price

    @Test
    public void matchTrades_matchesTrade_forSymbol_IfPricesWithinBounds() {
        Bid inputBid = Bid.builder().id(113).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();

        Ask inputAsk = Ask.builder().id(119).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        Trade trade1 = Trade.builder()
                .symbol("ABC")
                .bidId(113)
                .askId(119)
                .tradePrice(BigDecimal.valueOf(23.03))
                .build();

        List<Trade> expectedTrades = asList(trade1);
        when(tradeExecutionHelper.executeTrades(anyMap(), anyString())).thenReturn(expectedTrades);

        Map<QuotePrice, QuotePrice> tradeCandidateMap = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk);
        }};
        when(marketCheckHelper.checkMarket(anyList(), anyList())).thenReturn(tradeCandidateMap);

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputAsk));

        verify(tradeRepository).saveAll(expectedTrades);
        verify(marketCheckHelper).checkMarket(asList(inputBid), asList(inputAsk));
        verify(tradeExecutionHelper).executeTrades(tradeCandidateMap, "ABC");
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactlyInAnyOrder(trade1);
    }

    @Test
    public void matchTrades_doesNotCreateTradeIfBidAndAskPriceAreToFarApart() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Ask inputAsk = Ask.builder().id(14).symbol("ABC").askPrice(BigDecimal.valueOf(23.25)).build();

        when(marketCheckHelper.checkMarket(asList(inputBid), asList(inputAsk))).thenReturn(emptyMap());

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputAsk));

        verifyNoInteractions(tradeRepository);
        assertThat(actual).isNull();
    }


    @Test
    public void matchTrades_returnsListOfMultipleMatches_ifTradeGroupHasSizesThatMatch() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(23.02)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.04)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        Trade expectedTrade1 = Trade.builder()
                .bidId(73)
                .askId(56)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.02))
                .build();
        Trade expectedTrade2 = Trade.builder()
                .bidId(84)
                .askId(90)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.04))
                .build();

        Map<QuotePrice, QuotePrice> expectedMatches = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk);
            put(inputBid2, inputAsk2);
        }};
        when(marketCheckHelper.checkMarket(anyList(), anyList())).thenReturn(expectedMatches);

        List<Trade> expectedTrades = asList(expectedTrade1, expectedTrade2);
        when(tradeExecutionHelper.executeTrades(anyMap(), anyString())).thenReturn(expectedTrades);

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputBid2, inputAsk, inputAsk2));

        verify(tradeRepository).saveAll(expectedTrades);
        verify(marketCheckHelper).checkMarket(asList(inputBid, inputBid2), asList(inputAsk, inputAsk2));
        verify(tradeExecutionHelper).executeTrades(expectedMatches, "ABC");
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedTrades);
    }

    @Test
    public void matchTrades_doesNotCallRepoOrHelperIfMarketCheckHelperReturnsEmptyMap() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(23.60)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.18)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.99)).build();

        when(marketCheckHelper.checkMarket(asList(inputBid, inputBid2), asList(inputAsk, inputAsk2))).thenReturn(emptyMap());

        tradeMatcherService.matchTrades(asList(inputBid, inputBid2, inputAsk, inputAsk2));

        verifyNoInteractions(tradeRepository);
        verifyNoInteractions(tradeExecutionHelper);
    }

    @Test
    public void matchTrades_returnsListOfMultipleMatches_ifTradeGroupContainsOnlySomeMatches() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.75)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.20)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        Trade expectedTrade1 = Trade.builder()
                .bidId(73)
                .askId(90)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.03))
                .build();

        Map<QuotePrice, QuotePrice> expectedMatches = new HashMap<QuotePrice, QuotePrice>() {{
            put(inputBid, inputAsk2);
        }};
        when(marketCheckHelper.checkMarket(anyList(), anyList())).thenReturn(expectedMatches);

        List<Trade> expectedTrades = asList(expectedTrade1);
        when(tradeExecutionHelper.executeTrades(anyMap(), anyString())).thenReturn(expectedTrades);

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputBid2, inputAsk, inputAsk2));

        verify(tradeRepository).saveAll(expectedTrades);
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedTrades);
    }
}