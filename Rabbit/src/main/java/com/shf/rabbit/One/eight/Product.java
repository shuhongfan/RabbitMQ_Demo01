package com.shf.rabbit.One.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

public class Product {
    public static final String normal_exchange = "normal_exchange";


    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder()
                        .expiration("10000")
                        .build();

        for (int i = 1; i < 1000; i++) {
            String message = "info" + i;
            channel.basicPublish(normal_exchange, "zhangsan", properties, message.getBytes());
        }
    }
}
