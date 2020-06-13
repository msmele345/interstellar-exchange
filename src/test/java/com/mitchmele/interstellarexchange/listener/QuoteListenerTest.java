package com.mitchmele.interstellarexchange.listener;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.services.QuoteConverter;
import com.mitchmele.interstellarexchange.services.RealtimeTradeOrchestrator;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.JMSException;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class QuoteListenerTest {

    @Mock
    private QuoteConverter quoteConverter;

    @Mock
    private RealtimeTradeOrchestrator realtimeTradeOrchestrator;

    @InjectMocks
    private QuoteListener quoteListener;


    @Test
    public void onMessage_callsQuoteTransformer() throws JMSException {
        ActiveMQTextMessage mockMessage = mock(ActiveMQTextMessage.class);

        String incomingPayload = "{\"id\":162,\"symbol\":\"OGC\",\"bidPrice\":23.05,\"timeStamp\":1591614219318,\"price\":23.05}";
        when(mockMessage.getText()).thenReturn(incomingPayload);

        quoteListener.onMessage(mockMessage);

        verify(quoteConverter).convert(incomingPayload);
    }


    @Test
    public void onMessage_callsRealtimeTradeOrchestrator_withConvertedQuotes() throws JMSException {
        ActiveMQTextMessage mockMessage = mock(ActiveMQTextMessage.class);

        String incomingAsk = "{\"id\":162,\"symbol\":\"OGC\",\"bidPrice\":23.05,\"timeStamp\":1591614219318,\"price\":23.05}";
        String incomingBid = "{\"id\":162,\"symbol\":\"OGC\",\"bidPrice\":23.05,\"timeStamp\":1591614219318,\"price\":23.05}";
        when(mockMessage.getText())
                .thenReturn(incomingAsk)
                .thenReturn(incomingBid);

        Ask expectedAsk = Ask.builder()
                .id(162)
                .symbol("OGC")
                .askPrice(BigDecimal.valueOf(23.11))
                .build();


        Bid expectedBid = Bid.builder()
                .id(162)
                .symbol("OGC")
                .bidPrice(BigDecimal.valueOf(23.05))
                .build();

        when(quoteConverter.convert(any()))
                .thenReturn(expectedAsk)
                .thenReturn(expectedBid);

        quoteListener.onMessage(mockMessage);
        quoteListener.onMessage(mockMessage);

        verify(realtimeTradeOrchestrator).orchestrate(asList(expectedAsk, expectedBid));
    }
}
