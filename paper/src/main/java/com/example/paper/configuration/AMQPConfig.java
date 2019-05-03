package com.example.paper.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AMQPConfig {


    //消息json序列化
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue queue1(){
        return new Queue("mailQueue");
    }
    @Bean
    public DirectExchange directExchange1(){
        return new DirectExchange("mailExchange");
    }
    @Bean
    public Binding binding(Queue queue1, DirectExchange directExchange1){
        return BindingBuilder.bind(queue1).to(directExchange1).with("/mail");
    }

}
