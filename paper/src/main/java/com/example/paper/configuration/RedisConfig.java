package com.example.paper.configuration;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


//    @Bean
//    public <T>RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String,T> redisTemplate=new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(factory);
//        GenericJackson2JsonRedisSerializer serializer=new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setDefaultSerializer(serializer);
//        redisTemplate.setValueSerializer(serializer);
////        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
