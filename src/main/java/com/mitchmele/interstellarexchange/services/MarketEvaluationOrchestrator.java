package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.trade.TradeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketEvaluationOrchestrator {

    private final TradeMatcherService tradeMatcherService;
    private final MarketEvaluationService marketEvaluationService;
    private final TradeGroupMapper tradeGroupMapper;
    private final RealtimeMatcherService realtimeMatcherService; //change name to QuoteMatcherService

    public void orchestrateMarketEvaluation() {
        List<QuotePrice> allQuotes = marketEvaluationService.evaluate();
        List<TradeGroup> tradeGroups = tradeGroupMapper.mapQuotesToTradeGroups(allQuotes);

        realtimeMatcherService.matchRealTimeTrades(tradeGroups);
    }

    public void orchestrateEvaluationBySymbol(String symbol) {
        //use matcher service to match trades
        //obtains current quotes from quotePreProcessorService and passes to trade service
        List<QuotePrice> quotesForSymbol = marketEvaluationService.evaluateForSymbol(symbol);
        tradeMatcherService.matchTrades(quotesForSymbol);
    }
}
