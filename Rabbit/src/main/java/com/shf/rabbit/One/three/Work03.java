package com.shf.rabbit.One.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import com.shf.rabbit.One.utils.SleepUtils;
import lombok.SneakyThrows;

public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1等待接受消息时间较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接受到的消息：" + new String(message.getBody(), "UTF-8"));

            /**
             * 手动应答
             *
             * 1.消息的标记 tag
             * 2. 是否批量应答 false：不批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

//        设置不公平分发
//        int prefetchCount = 1;
//        预期值是5
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

//        采用手动确认模式
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }
}
