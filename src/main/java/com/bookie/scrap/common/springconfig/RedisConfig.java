package com.bookie.scrap.common.springconfig;

import com.bookie.scrap.common.redis.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, RedisProcessResult> redisStringTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisProcessResult> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<RedisProcessResult> serializer = new Jackson2JsonRedisSerializer<>(mapper, RedisProcessResult.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisHashService successBookCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.SUCCESS_BOOK);
    }

    @Bean
    public RedisHashService successDeckCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.SUCCESS_DECK);
    }

    @Bean
    public RedisHashService successUserCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.SUCCESS_USER);
    }

    @Bean
    public RedisHashService failedBookCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.FAILED_BOOK);
    }

    @Bean
    public RedisHashService failedDeckCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.FAILED_DECK);
    }

    @Bean
    public RedisHashService failedUserCode(RedisTemplate<String, RedisProcessResult> redisStringListTemplate) {
        return new RedisHashService(redisStringListTemplate, RedisHashNamespace.FAILED_USER);
    }


    @Bean
    public RedisTemplate<String, String> redisStringListTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisStringListService userCodeList(RedisTemplate<String, String> redisStringListTemplate) {
        return new RedisStringListService(redisStringListTemplate, RedisStringListNamespace.USER);
    }

    @Bean
    public RedisStringListService bookCodeList(RedisTemplate<String, String> redisStringListTemplate) {
        return new RedisStringListService(redisStringListTemplate, RedisStringListNamespace.BOOK);
    }

    @Bean
    public RedisStringListService deckCodeList(RedisTemplate<String, String> redisStringListTemplate) {
        return new RedisStringListService(redisStringListTemplate, RedisStringListNamespace.DECK);
    }

}
