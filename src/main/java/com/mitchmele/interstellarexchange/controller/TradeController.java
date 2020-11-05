package com.mitchmele.interstellarexchange.controller;

import com.mitchmele.interstellarexchange.helpers.TradesForDatesRequest;
import com.mitchmele.interstellarexchange.services.TradeLoaderService;
import com.mitchmele.interstellarexchange.trade.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TradeController {

    private final TradeLoaderService tradeLoaderService;

    @GetMapping("/trades")
    @CrossOrigin
    public List<Trade> getTrades() {
        return tradeLoaderService.fetchTrades();
    }

    @GetMapping("/trades/{symbol}")
    @CrossOrigin
    public List<Trade> getTradesBySymbol(@PathVariable String symbol) {
        return tradeLoaderService.fetchTradesForSymbol(symbol.toUpperCase());
    }

    @GetMapping("/trade/{id}")
    public Trade getTradeById(@PathVariable Integer id) {
        return tradeLoaderService.fetchTradeById(id);
    }

    @PostMapping("/trades")
    public List<Trade> getTradesByDateRange(@RequestBody TradesForDatesRequest tradesForDatesRequest) throws Exception {
        return tradeLoaderService.fetchTradesForDates(tradesForDatesRequest);
    }
}
