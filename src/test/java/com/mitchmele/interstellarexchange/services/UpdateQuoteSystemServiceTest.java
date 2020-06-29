package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import com.mitchmele.interstellarexchange.model.QuoteUpdateResult;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        updateQuoteSystemService.updateMarket(13, 15);
        verify(bidRepository).deleteById(13L);
        verify(askRepository).deleteById(15L);
    }

    @Test
    void updateMarket_returnsQuoteUpdateResultOfSuccessIfDeletesAreProcessed() {
        doNothing().when(bidRepository).deleteById(anyLong());
        doNothing().when(askRepository).deleteById(anyLong());

        QuoteUpdateResult expectedResult = QuoteUpdateResult.builder()
                .isSuccess(true)
                .build();
        QuoteUpdateResult actual = updateQuoteSystemService.updateMarket(13, 15);

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

        doThrow(new RuntimeException("error with bid")).when(bidRepository).deleteById(anyLong());

        QuoteUpdateResult actual = updateQuoteSystemService.updateMarket(13, 16);

        assertThat(actual).isEqualTo(expectedResult);
        verify(errorLogService).saveError(expectedErrorEntity);
    }
}