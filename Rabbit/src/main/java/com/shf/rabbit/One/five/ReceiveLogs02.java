package com.shf.rabbit.One.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME = "logs";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

//        声明一个队列， 临时队列
        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接后，队列就会自动删除
         */
        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上");

//        接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody()));
        };

//        消费者取消消息信息时的回调接口
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {

        });
    }

}
