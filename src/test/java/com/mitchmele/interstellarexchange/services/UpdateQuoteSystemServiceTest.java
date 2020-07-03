package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuoteUpdateResult;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateQuoteSystemServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AskRepository askRepository;

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private UpdateQuoteSystemService updateQuoteSystemService;

    @Test
    void updateMarket_removesBidAndAskFromQuoteSystemAfterTrade() {
        Ask askToBeDeleted = Ask.builder()
                .id(900)
                .askPrice(BigDecimal.valueOf(10.00))
                .build();

        Bid bidToBeDeleted = Bid.builder()
                .id(901)
                .bidPrice(BigDecimal.valueOf(11.00))
                .build();

        updateQuoteSystemService.updateMarket(bidToBeDeleted, askToBeDeleted);
        verify(bidRepository).delete(bidToBeDeleted);
        verify(askRepository).delete(askToBeDeleted);
    }

    @Test
    void updateMarket_returnsQuoteUpdateResultOfSuccessIfDeletesAreProcessed() {
        doNothing().when(bidRepository).delete(any());
        doNothing().when(askRepository).delete(any());

        QuoteUpdateResult expectedResult = QuoteUpdateResult.builder()
                .isSuccess(true)
                .build();

        Ask askToBeDeleted = Ask.builder()
                .id(900)
                .askPrice(BigDecimal.valueOf(10.00))
                .build();

        Bid bidToBeDeleted = Bid.builder()
                .id(901)
                .bidPrice(BigDecimal.valueOf(11.00))
                .build();

        QuoteUpdateResult actual = updateQuoteSystemService.updateMarket(bidToBeDeleted, askToBeDeleted);

        assertThat(actual).isEqualTo(expectedResult);
    }

    @Test
    void updateMarket_throwsIllegalArgumentExceptionIfBidOrAskIsNull() {
        QuoteUpdateResult expectedResult = QuoteUpdateResult.builder()
                .isSuccess(false)
                .lazyMessage("error with bid")
                .build();

        ErrorLogEntity expectedErrorEntity = ErrorLogEntity.builder()
                .serviceLocation(ServiceLocation.DATABASE.value)
                .errorMessage("error with bid")
                .errorType(ErrorType.PROCESSING.value)
                .build();

        doThrow(new RuntimeException("error with bid")).when(bidRepository).delete(any());

        Ask askToBeDeleted = Ask.builder()
                .id(900)
                .askPrice(BigDecimal.valueOf(10.00))
                .build();

        Bid bidToBeDeleted = Bid.builder()
                .id(901)
                .bidPrice(BigDecimal.valueOf(11.00))
                .build();

        QuoteUpdateResult actual = updateQuoteSystemService.updateMarket(bidToBeDeleted, askToBeDeleted);

        assertThat(actual).isEqualTo(expectedResult);
        verify(errorLogService).saveError(expectedErrorEntity);
    }
}