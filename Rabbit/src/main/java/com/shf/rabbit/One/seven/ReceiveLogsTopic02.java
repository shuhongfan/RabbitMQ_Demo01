package com.shf.rabbit.One.seven;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

/**
 * 主题交换机
 */
public class ReceiveLogsTopic02 {
    public static final String EXCHANGE_NAME = "logs_topic";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

//        声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

//        声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

//        接收消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
