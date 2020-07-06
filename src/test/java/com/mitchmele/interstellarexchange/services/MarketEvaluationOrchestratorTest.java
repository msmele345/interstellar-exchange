package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.QuoteTest;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketEvaluationOrchestratorTest extends QuoteTest {

    @Mock
    private MarketEvaluationService marketEvaluationService;

    @Mock
    private TradeMatcherService tradeMatcherService;

    @Mock
    private TradeGroupMapper tradeGroupMapper;

    @Mock
    private RealtimeMatcherService realtimeMatcherService;

    @InjectMocks
    private MarketEvaluationOrchestrator marketEvaluationOrchestrator;

    @Test
    public void orchestrateEvaluationBySymbol_callsQuoteServiceToObtainQuotesForProcessing() {
        marketEvaluationOrchestrator.orchestrateEvaluationBySymbol("ABC");
        verify(marketEvaluationService).evaluateForSymbol("ABC");
    }

    @Test
    void orchestrateEvaluationBySymbol_passesAllQuotesForSymbolToTradeMatcherService() {
        List<QuotePrice> expectedQuotesForABC = asList(inputBid1, inputBid2, inputAsk);
        when(marketEvaluationService.evaluateForSymbol(anyString()))
                .thenReturn(expectedQuotesForABC);

        marketEvaluationOrchestrator.orchestrateEvaluationBySymbol("ABC");

        ArgumentCaptor<List<QuotePrice>> captor = ArgumentCaptor.forClass(List.class);
        verify(tradeMatcherService).matchTrades(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedQuotesForABC);
    }

    @Test
    public void orchestrateMarketEvaluation_callsMarketEvaluationService() {
        marketEvaluationOrchestrator.orchestrateMarketEvaluation();
        verify(marketEvaluationService).evaluate();
    }

    @Test
    void orchestrateMarketEvaluation_callsQuoteMatcherServiceWithListOfAllQuotes() {
        TradeGroup abcTradeGroup = TradeGroup.builder().symbol("ABC").quotePrices(asList(inputBid1, inputBid2, inputAsk)).build();
        TradeGroup bbnTradeGroup = TradeGroup.builder().symbol("BBN").quotePrices(asList(inputBid3, inputAsk2)).build();
        TradeGroup cazTradeGroup = TradeGroup.builder().symbol("CAZ").quotePrices(asList(inputBid4, inputAsk3)).build();
        TradeGroup ogcTradeGroup = TradeGroup.builder().symbol("OGC").quotePrices(asList(inputBid5, inputAsk4, inputAsk5)).build();

        List<QuotePrice> allQuotes = asList(inputBid1, inputBid2, inputBid3, inputBid4, inputBid5, inputAsk, inputAsk2, inputAsk3, inputAsk4, inputAsk5);
        when(marketEvaluationService.evaluate()).thenReturn(allQuotes);

        List<TradeGroup> expectedTradeGroups = asList(abcTradeGroup, bbnTradeGroup, cazTradeGroup, ogcTradeGroup);
        when(tradeGroupMapper.mapQuotesToTradeGroups(anyList())).thenReturn(expectedTradeGroups);

        marketEvaluationOrchestrator.orchestrateMarketEvaluation();

        ArgumentCaptor<List<TradeGroup>> captor = ArgumentCaptor.forClass(List.class);

        verify(realtimeMatcherService).matchRealTimeTrades(captor.capture());
        assertThat(captor.getValue()).containsExactlyInAnyOrderElementsOf(expectedTradeGroups);
    }
}