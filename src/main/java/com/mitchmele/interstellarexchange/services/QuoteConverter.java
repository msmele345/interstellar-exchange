package com.mitchmele.interstellarexchange.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuotePrice;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuoteConverter implements Converter<String, QuotePrice> {

     private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public QuotePrice convert(String source) {
        JSONObject jsonObject = new JSONObject(source);
        boolean isBid = jsonObject.has("bidPrice");
        boolean isAsk = jsonObject.has("askPrice");

        try {
            if (isBid) {
                return objectMapper.readValue(source, Bid.class);
            }
            if (isAsk) {
                return objectMapper.readValue(source, Ask.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
