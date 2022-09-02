package com.shf.rabbit.One.two;

import com.rabbitmq.client.Channel;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

import java.util.Scanner;

/**
 * 生产者
 */
public class Task01 {

    public static final String QUEUE_NAME = "Hello";

    /**
     * 发送大量消息
     * @param args
     */
    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

//        队列的声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成");
        }
    }

}
