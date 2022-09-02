package com.shf.rabbit.One.eight;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

public class Consumer02 {

//    死信队列的名称
    public static final String dead_queue = "dead_queue";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("等待接收消息");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("Consumer02接收到的消息：" + new String(message.getBody(), "UTF-8"));
        });

        channel.basicConsume(dead_queue, true, deliverCallback, (consumerTag, sig) ->{});


    }
}
