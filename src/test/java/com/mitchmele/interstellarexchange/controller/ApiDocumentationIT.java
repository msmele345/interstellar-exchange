package com.mitchmele.interstellarexchange.controller;

import com.mitchmele.interstellarexchange.services.TradeLoaderService;
import com.mitchmele.interstellarexchange.trade.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Tag("Documentation")
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class ApiDocumentationIT {

    MockMvc mockMvc;

    @MockBean
    private TradeLoaderService tradeLoaderService;

    FieldDescriptor[] fields = {
            fieldWithPath("id").description("trade id"),
            fieldWithPath("symbol").description("stock symbol"),
            fieldWithPath("bidId").description("stock bid id"),
            fieldWithPath("askId").description("stock ask id"),
            fieldWithPath(".tradePrice").description("trade price"),
            fieldWithPath("timeStamp").description("time of trade")
    };


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }


    @Test
    public void getTradeByIdShouldReturnOk() throws Exception {

        Trade trade = Trade.builder()
                .id(1).bidId(2).askId(3)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(21.22))
                .timeStamp(new Date())
                .build();

        when(tradeLoaderService.fetchTradeById(anyInt())).thenReturn(trade);

        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/api/v1/trade/{id}", 1))
                .andExpect(status().isOk())
                .andDo(document(
                        "get-trade-by-id", // for index.adoc
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("The id of trade requested trade")),
                        responseFields(fields)
                ));
    }


    @Test
    void getTradesShouldReturnAllTrades() throws Exception {

        Trade trade = Trade.builder()
                .id(1).bidId(2).askId(3)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(21.22))
                .timeStamp(new Date())
                .build();

        Trade trade2 = Trade.builder()
                .id(2).bidId(10).askId(14)
                .symbol("DDY")
                .tradePrice(BigDecimal.valueOf(91.45))
                .timeStamp(new Date())
                .build();


        Trade trade3 = Trade.builder()
                .id(3).bidId(45).askId(98)
                .symbol("INTC")
                .tradePrice(BigDecimal.valueOf(24.16))
                .timeStamp(new Date())
                .build();

        List<Trade> trades = asList(trade, trade2, trade3);

        when(tradeLoaderService.fetchTrades())
                .thenReturn(trades);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trades"))
                .andExpect(status().isOk())
                .andDo(
                        document("get-trades",
                                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[]").description("A list of all trades at the exchange")
                                ).andWithPrefix("[].", fields) //after all response fields
                        ));

    }

    @Test
    void getTradesBySymbolShouldReturnAllTradesForSymbol() throws Exception {

        Trade trade = Trade.builder()
                .id(1).bidId(2).askId(3)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(21.22))
                .timeStamp(new Date())
                .build();

        Trade trade2 = Trade.builder()
                .id(2).bidId(10).askId(14)
                .symbol("ABC")
                .tradePrice(BigDecimal.valueOf(22.45))
                .timeStamp(new Date())
                .build();

        when(tradeLoaderService.fetchTradesForSymbol(anyString()))
                .thenReturn(asList(trade, trade2));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trades/{symbol}", "ABC"))
                .andExpect(status().isOk())
                .andDo(
                        document(
                               "get-trades-by-symbol",
                                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("symbol").description("The symbol of the stock")),
                                responseFields(
                                        fieldWithPath("[]").description("A list of all trades for a given symbol")
                                ).andWithPrefix("[].", fields))
                );
    }
}