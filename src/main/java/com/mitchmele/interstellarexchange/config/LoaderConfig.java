package com.mitchmele.interstellarexchange.config;


import com.mitchmele.interstellarexchange.listener.QuoteListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.Destination;

@Configuration
@EnableJms
public class LoaderConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${destination.jms.inbound}")
    private String stocks;

    @Bean
    Destination quoteDestination() {
        return new ActiveMQQueue(stocks);
    }

    //connection factory
    @Bean
    ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    //caching or default connection factory for mq

    @Bean
    CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(activeMQConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(10);
        return cachingConnectionFactory;
    }

    //jms template
    @Bean
    JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
        jmsTemplate.setDefaultDestination(quoteDestination());
        jmsTemplate.setReceiveTimeout(5000);
        return jmsTemplate;
    }

    @Bean
    DefaultMessageListenerContainer defaultMessageListenerContainer(
            QuoteListener quoteListener
    ) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setMessageListener(quoteListener);
        defaultMessageListenerContainer.setConnectionFactory(cachingConnectionFactory());
        defaultMessageListenerContainer.setMessageConverter(jacksonJmsMessageConverter());
//        defaultMessageListenerContainer.setErrorHandler(jmsQuoteErrorHandler);
        defaultMessageListenerContainer.setDestinationName(stocks);
        return defaultMessageListenerContainer;
    }


    //listener container?
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_id");
        return converter;
    }
}
