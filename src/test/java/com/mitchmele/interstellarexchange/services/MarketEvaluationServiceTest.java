package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
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
class MarketEvaluationServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AskRepository askRepository;

    @InjectMocks
    private MarketEvaluationService marketEvaluationService;


    public Bid inputBid1 = Bid.builder().id(162).symbol("ABC").bidPrice(BigDecimal.valueOf(23.05)).build(); //trade at 23.05
    public Bid inputBid2 = Bid.builder().id(163).symbol("ABC").bidPrice(BigDecimal.valueOf(23.07)).build(); //no trade, extra bid
    public Bid inputBid3 = Bid.builder().id(164).symbol("BBN").bidPrice(BigDecimal.valueOf(13.25)).build(); //trade at 13.275
    public Bid inputBid4 = Bid.builder().id(172).symbol("CAZ").bidPrice(BigDecimal.valueOf(3.06)).build();  //trade at 3.065
    public Bid inputBid5 = Bid.builder().id(22).symbol("OGC").bidPrice(BigDecimal.valueOf(113.03)).build(); //no trade

    public Ask inputAsk = Ask.builder().id(19).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();
    public Ask inputAsk2 = Ask.builder().id(20).symbol("BBN").askPrice(BigDecimal.valueOf(13.30)).build();
    public Ask inputAsk3 = Ask.builder().id(21).symbol("CAZ").askPrice(BigDecimal.valueOf(3.07)).build();
    public Ask inputAsk4 = Ask.builder().id(22).symbol("OGC").askPrice(BigDecimal.valueOf(113.03)).build();
    public Ask inputAsk5 = Ask.builder().id(23).symbol("OGC").askPrice(BigDecimal.valueOf(113.98)).build();

    @Test
    void evaluate() {
        when(bidRepository.findAll()).thenReturn(
                asList(inputBid1,
                        inputBid2,
                        inputBid3,
                        inputBid4,
                        inputBid5));

        when(askRepository.findAll()).thenReturn(
                asList(inputAsk,
                        inputAsk2,
                        inputAsk3,
                        inputAsk4,
                        inputAsk5));


        List<QuotePrice> expected = asList(inputAsk,
                inputAsk2,
                inputAsk3,
                inputAsk4,
                inputAsk5,
                inputBid1,
                inputBid2,
                inputBid3,
                inputBid4,
                inputBid5);

        List<QuotePrice> actual = marketEvaluationService.evaluate();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }


    @Test
    public void fetchQuotesBySymbol_callsReposForAllBidsAndAsks_forSymbol() {
        marketEvaluationService.evaluateForSymbol("OGC");
        verify(bidRepository).findAllBySymbol("OGC");
        verify(askRepository).findAllBySymbol("OGC");
    }

    @Test
    public void fetchQuotesBySymbol_returnsListOfAllBidsAndAsks_forSymbol() {

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

        marketEvaluationService.evaluateForSymbol("OGC");

        when(bidRepository.findAllBySymbol(anyString()))
                .thenReturn(asList(inputBid, inputBid2));

        when(askRepository.findAllBySymbol(anyString()))
                .thenReturn(asList(inputAsk, inputAsk2));

        List<QuotePrice> actual = marketEvaluationService.evaluateForSymbol("OGC");
        assertThat(actual).containsExactlyInAnyOrder(inputBid, inputAsk, inputAsk2, inputBid2);
    }
}