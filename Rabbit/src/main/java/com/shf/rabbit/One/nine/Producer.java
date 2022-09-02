package com.shf.rabbit.One.nine;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.shf.rabbit.One.utils.RabbitMQUtils;

public class Producer {
    private static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtils.getChannel();) {
            //给消息赋予一个 priority 属性
            AMQP.BasicProperties properties = new
                    AMQP.BasicProperties().builder().priority(5).build();

            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                if (i == 5) {
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}
