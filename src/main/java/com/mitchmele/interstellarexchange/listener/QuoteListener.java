package com.mitchmele.interstellarexchange.listener;

import com.mitchmele.interstellarexchange.model.QuotePrice;
import com.mitchmele.interstellarexchange.services.QuoteConverter;
import com.mitchmele.interstellarexchange.services.RealtimeTradeOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteListener implements MessageListener {

    private final QuoteConverter quoteConverter;
    private final RealtimeTradeOrchestrator realtimeTradeOrchestrator;

    protected List<QuotePrice> newQuotes = new ArrayList<>();

    @Override
    public void onMessage(Message message) {

        if (message instanceof ActiveMQTextMessage) {
            try {
                String quoteJson = ((ActiveMQTextMessage) message).getText();
                log.info("CONSUMED JMS INBOUND MESSAGE WITH PAYLOAD: " + ((ActiveMQTextMessage) message).getText());
                newQuotes.add(quoteConverter.convert(quoteJson));
                //remove size constraint
                if (newQuotes.size() == 10) { //modify size matching constraint as needed
                    realtimeTradeOrchestrator.processRealTimeQuotes(newQuotes);
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
