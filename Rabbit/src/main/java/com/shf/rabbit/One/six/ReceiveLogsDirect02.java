package com.shf.rabbit.One.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

public class ReceiveLogsDirect02 {
    private static final String EXCHANGE_NAME = "direct_logs";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

//        声明一个队列
        channel.queueDeclare("disk",false, false, false, null);

        channel.queueBind("disk", EXCHANGE_NAME, "error");

//        接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };

//        消费者取消消息回调接口
        channel.basicConsume("disk", true, deliverCallback,consumerTag -> {});
    }
}
