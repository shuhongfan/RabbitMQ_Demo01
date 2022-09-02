package com.shf.rabbit.One.one;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

public class Consumer {
    //    队列的名称
    public static final String QUEUE_NAME = "Hello";

    //    接收消息
    @SneakyThrows
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.120.20");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

//        消息成功接收后的回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received '" + message + "'");
        };

//        消息接收失败的回调
        CancelCallback cancelCallback= consumerTag -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费那个队列
         * 2.消费成功之后是否需要自动应答 true自动应答 false手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        String msg = channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println(msg);
    }
}
