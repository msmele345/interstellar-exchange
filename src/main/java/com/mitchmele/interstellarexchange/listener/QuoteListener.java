package com.mitchmele.interstellarexchange.listener;

import com.mitchmele.interstellarexchange.quote.QuotePrice;
import com.mitchmele.interstellarexchange.services.QuoteConverter;
import com.mitchmele.interstellarexchange.services.RealtimeTradeOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteListener implements MessageListener {

    private final QuoteConverter quoteConverter;
    private final RealtimeTradeOrchestrator realtimeTradeOrchestrator;

    List<QuotePrice> newQuotes = new ArrayList<>();

    @Override
    public void onMessage(Message message) {
        if (message instanceof ActiveMQTextMessage) {
            try {
                String quoteJson = ((ActiveMQTextMessage) message).getText();
                log.info("CONSUMED JMS INBOUND MESSAGE WITH PAYLOAD: " + ((ActiveMQTextMessage) message).getText());
                newQuotes.add(quoteConverter.convert(quoteJson));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedDelay = 30000L) //tweak to 30 seconds to allow for all quotes to go through trade flow?
    public void processQuotes() {
        if (!newQuotes.isEmpty()) {
            realtimeTradeOrchestrator.processRealTimeQuotes(newQuotes);
            newQuotes.clear();
        }
    }
}