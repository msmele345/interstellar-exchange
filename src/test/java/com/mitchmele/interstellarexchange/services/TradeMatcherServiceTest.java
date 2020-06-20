package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.QuoteTest;
import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.Trade;
import com.mitchmele.interstellarexchange.model.TradeGroup;
import com.mitchmele.interstellarexchange.repository.TradeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TradeMatcherServiceTest extends QuoteTest {

    @Mock
    private TradeRepository tradeRepository;

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

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputAsk));

        verify(tradeRepository).saveAll(asList(trade1));
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactlyInAnyOrder(trade1);
    }

    @Test
    public void matchTrades_doesNotCreateTradeIfBidAndAskPriceAreToFarApart() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Ask inputAsk = Ask.builder().id(14).symbol("ABC").askPrice(BigDecimal.valueOf(23.04)).build();

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputAsk));

        verifyNoInteractions(tradeRepository);
        assertThat(actual).isEmpty();
    }


    @Test
    public void matchTrades_returnsListOfMultipleMatches_ifTradeGroupHasSizesThatMatch() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(23.02)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.04)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputBid2, inputAsk, inputAsk2));

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

        assertThat(actual).containsExactlyInAnyOrderElementsOf(asList(expectedTrade1, expectedTrade2));
    }

    @Test
    @Disabled
    public void matchTrades_returnsListOfMultipleMatches_ifTradeGroupContainsOnlySomeMatches() {
        Bid inputBid = Bid.builder().id(73).symbol("ABC").bidPrice(BigDecimal.valueOf(23.00)).build();
        Bid inputBid2 = Bid.builder().id(84).symbol("ABC").bidPrice(BigDecimal.valueOf(22.75)).build();
        Ask inputAsk = Ask.builder().id(56).symbol("ABC").askPrice(BigDecimal.valueOf(23.20)).build();
        Ask inputAsk2 = Ask.builder().id(90).symbol("ABC").askPrice(BigDecimal.valueOf(23.06)).build();

        List<Trade> actual = tradeMatcherService.matchTrades(asList(inputBid, inputBid2, inputAsk, inputAsk2));

        Trade expectedTrade1 = Trade.builder()
                .bidId(73)
                .askId(90)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.03))
                .build();

        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(asList(expectedTrade1));
    }

    @Test
    public void matchRealTimeTrades_shouldHandleMultipleBidsAndOffersWithDifferentSymbolsAndSize() {

        TradeGroup abcTradeGroup = TradeGroup.builder().symbol("ABC").quotePrices(asList(inputBid1, inputBid2, inputAsk)).build();
        TradeGroup bbnTradeGroup = TradeGroup.builder().symbol("BBN").quotePrices(asList(inputBid3, inputAsk2)).build();
        TradeGroup cazTradeGroup = TradeGroup.builder().symbol("CAZ").quotePrices(asList(inputBid4, inputAsk3)).build();
        TradeGroup ogcTradeGroup = TradeGroup.builder().symbol("OGC").quotePrices(asList(inputBid5, inputAsk4, inputAsk5)).build();

        List<TradeGroup> inboundGroups = asList(abcTradeGroup, bbnTradeGroup, cazTradeGroup, ogcTradeGroup);

        Trade trade1 = Trade.builder().symbol("OGC").bidId(22).askId(22).tradePrice(BigDecimal.valueOf(113.03)).build();
        Trade trade2 = Trade.builder().symbol("ABC").bidId(162).askId(19).tradePrice(BigDecimal.valueOf(23.05)).build();
        Trade trade3 = Trade.builder().symbol("BBN").bidId(164).askId(20).tradePrice(BigDecimal.valueOf(13.25)).build();
        Trade trade4 = Trade.builder().symbol("CAZ").bidId(172).askId(21).tradePrice(BigDecimal.valueOf(3.06)).build();

        List<Trade> expectedTrades = asList(trade1, trade2, trade3, trade4);

        List<Trade> actual = tradeMatcherService.matchRealTimeTrades(inboundGroups);

        verify(tradeRepository).saveAll(asList(trade1));
        verify(tradeRepository).saveAll(asList(trade2));
        verify(tradeRepository).saveAll(asList(trade3));
        verify(tradeRepository).saveAll(asList(trade4));
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedTrades);

    }

    @Test
    public void marketDepthCheck_returnsClassNameOfSmallerSizeOfLevel2Depth() {
        String actual = tradeMatcherService.marketDepthCheck(
                asList(inputBid1, inputBid2),
                asList(inputAsk, inputAsk2, inputAsk3));

        String expected = "BID";

        assertThat(actual).isEqualTo(expected);
    }
}