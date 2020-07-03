package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradeExecutionHelper {

    public List<Trade> executeTrades(Map<QuotePrice, QuotePrice> bidsAndOffers, String symbol) {
         return bidsAndOffers.entrySet().stream()
                .map(priceEntry ->
                        Trade.builder()
                                .symbol(symbol)
                                .bidId(priceEntry.getKey().getId())
                                .askId(priceEntry.getValue().getId())
                                .tradePrice(
                                        getFillPrice(priceEntry.getKey().getPrice(),
                                                priceEntry.getValue().getPrice()))
                                .build()
                ).collect(Collectors.toList());
    }
    //executes at the midpoint between bid and ask if trade eligable
    private BigDecimal getFillPrice(BigDecimal bidPrice, BigDecimal askPrice) {
        double diff = (askPrice.doubleValue() - bidPrice.doubleValue()) / 2;
        BigDecimal fillprice = BigDecimal.valueOf(bidPrice.doubleValue() + diff);
        fillprice.setScale(2, RoundingMode.HALF_DOWN);
        return fillprice;
    }
}
