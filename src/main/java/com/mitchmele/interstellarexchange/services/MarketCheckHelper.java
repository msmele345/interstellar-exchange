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
        //feed this into matchTrades
        //OR feed into new service/method that takes a map param and creates trades then saves to the repo
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
