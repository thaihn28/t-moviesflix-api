//package com.example.tmovierestapi.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory();
//    }
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate() {
//        final RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//
//        // the following is not required
//        template.setHashValueSerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.afterPropertiesSet();
//        return template;
//    }
//}
