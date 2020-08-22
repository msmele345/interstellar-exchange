package com.mitchmele.interstellarexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.interstellarexchange.common.InvalidDateRequestException;
import com.mitchmele.interstellarexchange.helpers.TradesForDatesRequest;
import com.mitchmele.interstellarexchange.services.TradeLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.persistence.EntityNotFoundException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestAdviceTest {

    @Mock
    private TradeLoaderService tradeLoaderService;

    @InjectMocks
    private TradeController tradeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(tradeController)
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    @Test
    void handleException() throws Exception {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("RuntimeException")
                .exceptionMsg("There was an error processing the request")
                .build();

        when(tradeLoaderService.fetchTrades()).thenThrow(new RuntimeException("bad error"));

        ObjectMapper mapper = new ObjectMapper();

        String expected = mapper.writeValueAsString(errorResponse);

        mockMvc
                .perform(get("/api/v1/trades"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expected));
    }

    @Test
    void handleNotFound() throws Exception {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("EntityNotFoundException")
                .exceptionMsg("There was an error processing the request")
                .build();

        when(tradeLoaderService.fetchTradesForSymbol(anyString()))
                .thenThrow(new EntityNotFoundException());

        ObjectMapper mapper = new ObjectMapper();

        String expected = mapper.writeValueAsString(errorResponse);

        mockMvc
                .perform(get("/api/v1/trades/ABC"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expected));
    }

    @Test
    void handleBadRequest() throws Exception {

        TradesForDatesRequest tradesForDatesRequest = TradesForDatesRequest.builder()
                .startDate("07-0sdf6-2020")
                .endDate("07-08-2020")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String inputRequest = mapper.writeValueAsString(tradesForDatesRequest);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("InvalidDateRequestException")
                .exceptionMsg("There was an error processing the request")
                .build();

        when(tradeLoaderService.fetchTradesForDates(any(TradesForDatesRequest.class)))
                .thenThrow(InvalidDateRequestException.class);

        mockMvc
                .perform(post("/api/v1/trades")
                        .content(inputRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mapper.writeValueAsString(errorResponse)));
    }
}