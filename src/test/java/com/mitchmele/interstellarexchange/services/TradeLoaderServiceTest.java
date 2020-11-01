package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.InvalidDateRequestException;
import com.mitchmele.interstellarexchange.helpers.DateHelper;
import com.mitchmele.interstellarexchange.helpers.TradesForDatesRequest;
import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import com.mitchmele.interstellarexchange.trade.Trade;
import com.mitchmele.interstellarexchange.trade.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeLoaderServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private DateHelper dateHelper;

    @Mock
    private LoaderAuditLogRepository loaderAuditLogRepository;

    @InjectMocks
    private TradeLoaderService tradeLoaderService;

    @Test
    void fetchTrades_returnsAllTrades() {

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

        Trade expectedTrade3 = Trade.builder()
                .bidId(3)
                .askId(4)
                .symbol("BBY")
                .tradePrice(BigDecimal.valueOf(2.27))
                .build();

        Trade expectedTrade4 = Trade.builder()
                .bidId(7)
                .askId(1)
                .symbol("BBY")
                .tradePrice(BigDecimal.valueOf(2.28))
                .build();

        List<Trade> expected = asList(expectedTrade, expectedTrade2, expectedTrade3, expectedTrade4);
        when(tradeRepository.findAll()).thenReturn(expected);


        List<Trade> actual = tradeLoaderService.fetchTrades();

        LoaderAuditLog loaderAuditLog = LoaderAuditLog.builder().loadCount(4).build();

        verify(loaderAuditLogRepository).save(loaderAuditLog);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fetchTradesForSymbol() {
        Trade expectedTrade = Trade.builder()
                .id(1)
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.06))
                .build();

        Trade expectedTrade2 = Trade.builder()
                .id(2)
                .bidId(73)
                .askId(14)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.275))
                .build();

        List<Trade> expected = asList(expectedTrade, expectedTrade2);

        when(tradeRepository.findAllBySymbol(anyString())).thenReturn(Optional.of(expected));

        List<Trade> actual = tradeLoaderService.fetchTradesForSymbol("ABC");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fetchTradesForSymbol_throwsEntityNotFoundException_whenNoTradesAreFoundForSymbol() {

        when(tradeRepository.findAllBySymbol(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeLoaderService.fetchTradesForSymbol("B"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void fetchTradesForDates() throws Exception {

        Date dateFromTrade1 = dateHelper.createDateFromString("07-06-2020");
        Date dateFromTrade2 = dateHelper.createDateFromString("07-08-2020");

        TradesForDatesRequest tradesForDatesRequest = TradesForDatesRequest.builder()
                .startDate("07-06-2020")
                .endDate("07-08-2020")
                .build();

        Trade expectedTrade = Trade.builder()
                .id(1)
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.06))
                .timeStamp(dateFromTrade1)
                .build();

        Trade expectedTrade2 = Trade.builder()
                .id(2)
                .bidId(73)
                .askId(14)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.275))
                .timeStamp(dateFromTrade2)
                .build();

        when(dateHelper.createDateFromString(anyString()))
                .thenReturn(new Date(1))
                .thenReturn(new Date(25));

        when(tradeRepository.findAllByTimeStampBetween(any(Date.class), any(Date.class)))
                .thenReturn(asList(expectedTrade, expectedTrade2));

        List<Trade> actual = tradeLoaderService.fetchTradesForDates(tradesForDatesRequest);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(asList(expectedTrade, expectedTrade2));
    }

    @Test
    void fetchTradesForDates_throwsParseExceptionWhenDatesAreMalformed() throws Exception {
        TradesForDatesRequest tradesForDatesRequest = TradesForDatesRequest.builder()
                .startDate("07-066-2020")
                .endDate("07-08-2020")
                .build();

        when(dateHelper.createDateFromString(anyString()))
                .thenThrow(new RuntimeException("unable to parse date"));


        assertThatThrownBy(() -> tradeLoaderService.fetchTradesForDates(tradesForDatesRequest))
                .isInstanceOf(InvalidDateRequestException.class)
                .hasMessage("Bad Request");
    }

    @Test
    void fetchTradeById_returnsOptionOfTradeIfFound() {

        Date mockTimestamp = mock(Date.class);

        Trade expectedTrade = Trade.builder()
                .id(1)
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.06))
                .timeStamp(mockTimestamp)
                .build();

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(expectedTrade));

        Trade actual = tradeLoaderService.fetchTradeById(1);
        assertThat(actual).isEqualTo(expectedTrade);
    }

    @Test
    void fetchTradesById_throwsEntityNotFoundException_ifOptionalIsEmpty() {

        when(tradeRepository.findById(anyInt()  )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeLoaderService.fetchTradeById(2))
                .isInstanceOf(EntityNotFoundException.class);
    }
}