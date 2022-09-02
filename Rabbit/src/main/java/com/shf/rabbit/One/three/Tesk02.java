package com.shf.rabbit.One.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

import java.util.Scanner;

public class Tesk02 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

        boolean durable = true; // 是否持久化
        channel.queueDeclare(
                TASK_QUEUE_NAME,
                durable,
                false,
                false,
                null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("",
                    TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println("生产者发出消息：" + message);
        }
    }
}
