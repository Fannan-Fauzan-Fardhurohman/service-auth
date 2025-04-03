package com.fanxan.serviceauth.config;


import com.fanxan.serviceauth.exception.ListenerRetryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.hostname:#{null}}")
    private String hostname;

    @Value("${rabbitmq.port:5672}")
    private Integer port;

    @Value("${rabbitmq.username:#{null}}")
    private String username;

    @Value("${rabbitmq.password:#{null}}")
    private String password;

    @Value("${rabbitmq.retry:#{3}}")
    private Integer retry;

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostname, port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);

        return cachingConnectionFactory;
    }

    @Bean
    Declarables queues() {
        Map<String, Object> argumentsAuth = new HashMap<>();
        argumentsAuth.put("x-message-ttl", 60 * 1000); // 1 Minutes TTL

        Map<String, Object> argumentsOrders = new HashMap<>();
        argumentsOrders.put("x-message-ttl", 60 * 1000); // 1 Minutes TTL
        argumentsOrders.put("x-dead-letter-exchange", "auth.dlx");
        argumentsOrders.put("x-dead-letter-routing-key", "AUTH.DLX");

        return new Declarables(
                new Queue("auth", true, false, false, argumentsAuth)
        );
    }

    @Bean
    Declarables exchanges() {
        return new Declarables(
                new TopicExchange("auth"),
                new TopicExchange("auth.dlx")
        );
    }

    @Bean
    Declarables bindings() {
        return new Declarables(
                new Binding("auth", Binding.DestinationType.QUEUE, "auth", "AUTH", null)
        );
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor(RetryTemplate retryTemplate) {
        return RetryInterceptorBuilder.stateless()
                .retryOperations(retryTemplate)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory retryContainerFactory(
            ConnectionFactory connectionFactory,
            RetryOperationsInterceptor retryInterceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryInterceptor);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(ListenerRetryException.class, true);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(retry, retryableExceptions, true);
        retryTemplate.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

//        // Use this config if the interval of the retry is fixed
//        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//        backOffPolicy.setBackOffPeriod(2000); // 2 seconds delay between retries
//        retryTemplate.setBackOffPolicy(backOffPolicy);

//        // Add logging to the retry process
//        retryTemplate.registerListener(new RetryListener() {
//            @Override
//            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//                log.warn("Retry attempt #{} for exception: {}", context.getRetryCount(), throwable.getMessage());
//            }
//
//            @Override
//            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//                if (throwable != null) {
//                    log.error("Retry attempts exhausted: {}", throwable.getMessage());
//                }
//            }
//        });

        return retryTemplate;
    }

}
