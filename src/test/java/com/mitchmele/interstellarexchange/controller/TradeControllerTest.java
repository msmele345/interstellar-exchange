package com.mitchmele.interstellarexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.interstellarexchange.helpers.TradesForDatesRequest;
import com.mitchmele.interstellarexchange.services.TradeLoaderService;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TradeControllerTest {

    @Mock
    private TradeLoaderService tradeLoaderService;

    @InjectMocks
    private TradeController tradeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    void loadAllTrades_fetchesAllTrades() throws Exception {

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
        when(tradeLoaderService.fetchTrades()).thenReturn(expected);

        ObjectMapper mapper = new ObjectMapper();
        String expectedResponse = mapper.writeValueAsString(expected);


        mockMvc
                .perform(get("/api/v1/trades"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));

        verify(tradeLoaderService).fetchTrades();
    }

    @Test
    void getTradesBySymbol() throws Exception {

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

        List<Trade> expected = asList(expectedTrade, expectedTrade2);
        when(tradeLoaderService.fetchTradesForSymbol(anyString())).thenReturn(expected);

        ObjectMapper mapper = new ObjectMapper();
        String expectedResponse = mapper.writeValueAsString(expected);

        mockMvc
                .perform(get("/api/v1/trades/abc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));

        verify(tradeLoaderService).fetchTradesForSymbol("ABC");
    }

    @Test
    void getTradesByDateRange_returnsListOfTradesForRequestedDateRange() throws Exception {

        TradesForDatesRequest tradesForDatesRequest = TradesForDatesRequest.builder()
                .startDate("07-06-2020")
                .endDate("07-08-2020")
                .build();

        Trade expectedTrade = Trade.builder()
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .timeStamp(new Date(2))
                .tradePrice(BigDecimal.valueOf(23.06))
                .build();

        Trade expectedTrade2 = Trade.builder()
                .bidId(73)
                .askId(14)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(23.275))
                .timeStamp(new Date(1))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String inputRequest = mapper.writeValueAsString(tradesForDatesRequest);

        when(tradeLoaderService.fetchTradesForDates(any(TradesForDatesRequest.class)))
                .thenReturn(asList(expectedTrade, expectedTrade2));

        String expectedResponse = mapper.writeValueAsString(asList(expectedTrade, expectedTrade2));

        mockMvc
                .perform(post("/api/v1/trades")
                        .content(inputRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void getTradeById() throws Exception {

        Trade expectedTrade = Trade.builder()
                .id(2)
                .bidId(84)
                .askId(56)
                .symbol("ABC")
                .timeStamp(new Date(2))
                .tradePrice(BigDecimal.valueOf(23.06))
                .build();

        ObjectMapper mapper = new ObjectMapper();

        when(tradeLoaderService.fetchTradeById(any()))
                .thenReturn(expectedTrade);

        String expectedResponse = mapper.writeValueAsString(expectedTrade);

        mockMvc
                .perform(get("/api/v1/trade/2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));
    }
}