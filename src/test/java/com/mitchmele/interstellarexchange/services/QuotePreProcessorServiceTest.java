package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuotePreProcessorServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AskRepository askRepository;

    @InjectMocks
    private QuotePreProcessorService quotePreProcessorService;

    Bid inputBid = Bid.builder()
            .id(162)
            .symbol("OGC")
            .bidPrice(BigDecimal.valueOf(23.05))
            .build();

    Bid inputBid2 = Bid.builder()
            .id(163)
            .symbol("OGC")
            .bidPrice(BigDecimal.valueOf(23.07))
            .build();


    Ask inputAsk = Ask.builder()
            .id(16)
            .symbol("OGC")
            .askPrice(BigDecimal.valueOf(23.05))
            .build();

    Ask inputAsk2 = Ask.builder()
            .id(13)
            .symbol("OGC")
            .askPrice(BigDecimal.valueOf(23.07))
            .build();

    Ask inputAsk3 = Ask.builder()
            .id(11)
            .symbol("OGC")
            .askPrice(BigDecimal.valueOf(23.11))
            .build();


    @Test
    public void fetchQuotesBySymbol_callsReposForAllBidsAndAsks_forSymbol() {
        quotePreProcessorService.fetchQuotesForSymbol("OGC");
        verify(bidRepository).findAllBySymbol("OGC");
        verify(askRepository).findAllBySymbol("OGC");
    }

    @Test
    public void fetchQuotesBySymbol_returnsListOfAllBidsAndAsks_forSymbol() {
        quotePreProcessorService.fetchQuotesForSymbol("OGC");

        when(bidRepository.findAllBySymbol(anyString()))
                .thenReturn(asList(inputBid, inputBid2));

        when(askRepository.findAllBySymbol(anyString()))
                .thenReturn(asList(inputAsk, inputAsk2));

        List<QuotePrice> actual = quotePreProcessorService.fetchQuotesForSymbol("OGC");
        assertThat(actual).containsExactlyInAnyOrder(inputBid, inputAsk, inputAsk2, inputBid2);
    }

}