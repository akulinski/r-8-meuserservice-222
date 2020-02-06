package com.akulinski.r8meservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfiguration {

    @Value("${properties.exchange}")
    private String exchange;

    @Value("${properties.rateStore}")
    private String rateStore;

    @Value("${properties.rateQueue}")
    private String rateQueue;

    @Value("${properties.dataRequestsQueue}")
    private String dataRequests;

    @Value("${properties.dataRequestsExchange}")
    private String dataRequestsExchange;

    @Value("${properties.dataResponsesQueue}")
    private String dataResponses;

    @Value("${properties.dataResponsesExchange}")
    private String dataResponsesExchange;

    @Bean
    public FanoutExchange topicExchange() {
        return new FanoutExchange(exchange);
    }

    @Bean
    public Queue rateQueue() {
        return new Queue(rateQueue);
    }

    @Bean
    public Queue ratesToStore() {
        return new Queue(rateStore);
    }

    @Bean
    public TopicExchange topicExchangeDataRequests() {
        return new TopicExchange(dataRequestsExchange);
    }

    @Bean
    public Queue dataRequestsQueue() {
        return new Queue(dataRequests);
    }

    @Bean
    public TopicExchange topicExchangeDataResponses() {
        return new TopicExchange(dataResponsesExchange);
    }

    @Bean
    public Queue dataResponsesQueue() {
        return new Queue(dataResponses);
    }

    @Bean
    public Binding bindRateQueue() {
        return BindingBuilder.bind(rateQueue()).to(topicExchange());
    }

    @Bean
    public Binding bindRateQueueReplicatedRates() {
        return BindingBuilder.bind(ratesToStore()).to(topicExchange());
    }

    @Bean
    public Binding bindDataRequestsQueue() {
        return BindingBuilder.bind(dataRequestsQueue()).to(topicExchangeDataRequests()).with(RoutingKey.ANY);
    }

    @Bean
    public Binding bindDataResponsesExchange() {
        return BindingBuilder.bind(dataResponsesQueue()).to(topicExchangeDataResponses()).with(RoutingKey.ANY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter(getObjectMapper());
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper().findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
