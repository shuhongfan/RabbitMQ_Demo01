package com.shf.rabbit.One.five;

import com.rabbitmq.client.Channel;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

import java.util.Scanner;

public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME, "fauout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println("生产者发出消息："+message);
        }
    }
}
