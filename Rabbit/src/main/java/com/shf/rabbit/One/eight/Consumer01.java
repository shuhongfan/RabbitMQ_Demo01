package com.shf.rabbit.One.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

import java.util.HashMap;

public class Consumer01 {
    //    普通交换机名称
    public static final String normal_exchange = "normal_exchange";

    //    死信交换机名称
    public static final String dead_exchange = "dead_exchange";

    //    普通队列的名称
    public static final String normal_queue = "normal_queue";

    //    死信队列的名称
    public static final String dead_queue = "dead_queue";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();

//        声明死信交换机和普通交换机，类型为direct
        channel.exchangeDeclare(normal_exchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(dead_exchange, BuiltinExchangeType.DIRECT);

//        声明普通队列
        HashMap<String, Object> arguments = new HashMap<>();
//        过期时间
//        arguments.put("x-message-ttl", 10000);
//        正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", dead_exchange);
//        设置死信Routingkey
        arguments.put("x-dead-letter-routing-key", "lisi");
//        设置正常队列的长度限制
        arguments.put("x-max-lenth", 6);

        channel.queueDeclare(normal_queue, false, false, false, null);

//        声明死信队列
        channel.queueDeclare(dead_queue, false, false, false, null);

//        绑定普通的交换机与普通的队列
        channel.queueBind(normal_queue, normal_exchange, "zhangsan");
//        绑定死信的交换机与死信的队列
        channel.queueBind(dead_queue, dead_exchange, "lisi");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume(normal_queue, true, deliverCallback, (consumerTag, sig) -> {
        });
    }
}
