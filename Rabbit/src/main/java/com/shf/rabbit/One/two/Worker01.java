package com.shf.rabbit.One.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

/**
 * 这是一个工作线程（相当于之前的消费者）
 */
public class Worker01 {
    public static final String QUEUE_NAME = "Hello";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

        /**
         * 消息的接收
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        /**
         * 消息的取消接收
         */
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("Consumer cancelled");
        };

        /**
         * 消费者消费消息
         */
        System.out.println("C2等待接收消息");
        String res = channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println(res);
    }
}
