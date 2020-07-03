package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@NoArgsConstructor
public class MarketCheckHelper {

    public Map<QuotePrice, QuotePrice> checkMarket(List<QuotePrice> bids, List<QuotePrice> asks) {
        //check bid and ask price for symbol and check if a trade is possible using threshold
        //passes trade eligible map back to tradeMatcher service
        Map<QuotePrice, QuotePrice> result = new HashMap<>();
        asks.forEach(ask -> {
            double askPrice = ask.getPrice().doubleValue();
            double threshold = askPrice - (askPrice * .005);
            for (QuotePrice bid : bids) {
                double bidPrice = bid.getPrice().doubleValue();
                if (bidPrice >= threshold && !result.containsValue(ask) && !result.containsKey(bid)) {
                    result.put(bid, ask);
                }
            }
        });
        return result;
    }

}
