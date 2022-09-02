package com.shf.rabbit.One.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

public class Producer {
    public static final String QUEUE_NAME = "Hello";

    @SneakyThrows
    public static void main(String[] args) {
//        创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        工厂IP
        factory.setHost("192.168.120.20");
        factory.setUsername("guest");
        factory.setPassword("guest");

//        创建连接
        Connection connection = factory.newConnection();

//        获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘），默认情况存储在内存中
         * 3.该队列是否提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费
         * 4.是否自动删除，周会一个消费者开连接以后，该队一句是否自动删除 true自动删除 FALSE不自动删除
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

//        发消息
        String message = "hello world";

        /**
         * 发送一个消费
         * 1.发送到那个交换机
         * 2.路由的key值是那个 本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功");
    }
}
