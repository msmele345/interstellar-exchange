package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.Trade;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateQuoteSystemServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AskRepository askRepository;

    @Mock
    private UsedQuoteHelper usedQuoteHelper;

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private UpdateQuoteSystemService updateQuoteSystemService;

    @Test
    void updateMarket_removesBidAndAskFromQuoteSystemAfterTrade() {
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

        List<QuotePrice> bidsAndAsksUsedInTrades = asList(inputBid, inputAsk2);
        when(usedQuoteHelper.fetchUsedQuotes(anyList(), anyList())).thenReturn(bidsAndAsksUsedInTrades);

        updateQuoteSystemService.updateMarket(allQuotes, actualTrades);

        verify(usedQuoteHelper).fetchUsedQuotes(allQuotes, actualTrades);
        verify(bidRepository).delete(inputBid);
        verify(askRepository).delete(inputAsk2);
    }

    @Test
    void updateMarket_savesErrorLogEntityWithExceptionMetadataIfErrorOccurs() {

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

        List<QuotePrice> bidsAndAsksUsedInTrades = asList(inputBid, inputAsk2);
        when(usedQuoteHelper.fetchUsedQuotes(anyList(), anyList())).thenReturn(bidsAndAsksUsedInTrades);

        doThrow(new RuntimeException("error")).when(bidRepository).delete(any());

        ErrorLogEntity expectedErrorEntity = ErrorLogEntity.builder()
                .serviceLocation(ServiceLocation.DATABASE.value)
                .errorMessage("error")
                .errorType(ErrorType.PROCESSING.value)
                .build();

        updateQuoteSystemService.updateMarket(allQuotes, actualTrades);

        verify(errorLogService).saveError(expectedErrorEntity);
    }
}