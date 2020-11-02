package com.mitchmele.interstellarexchange.controller;

import com.mitchmele.interstellarexchange.services.TradeLoaderService;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
@AutoConfigureRestDocs("build/snippets")
@Tag("IT")
class TradeControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeLoaderService tradeLoaderService;


    @Test
    void getTradeById_returnsCorrectTrade() throws Exception {

        Trade trade = Trade.builder().id(1).symbol("ABC").tradePrice(BigDecimal.valueOf(21.22)).build();
        when(tradeLoaderService.fetchTradeById(anyInt())).thenReturn(trade);

        mockMvc.perform(
                get("/api/v1/trade/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("trade",
                        responseFields(
                                fieldWithPath("id").description("trade id"),
                                fieldWithPath("symbol").description("stock symbol"),
                                fieldWithPath("bidId").description("stock bid id"),
                                fieldWithPath("askId").description("stock ask id"),
                                fieldWithPath(".tradePrice").description("trade price"),
                                fieldWithPath("timeStamp").description("time of trade")))
                );
    }
}

