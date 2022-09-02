package com.shf.rabbit.One.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import com.shf.rabbit.One.utils.RabbitMQUtils;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    //    批量发消息的个数
    public static final int BATCH_SIZE = 1000;

    public static void main(String[] args) {
//        1.单个确认
//        publishMessageIndividually();

//            2.批量确认
//        publishMessageBatch();

//        3.异步批量确认
        publicMessageAsync();
    }

    //    异步批量确认
    @SneakyThrows
    public static void publicMessageAsync() {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

//        开启发布确认机制
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的将需要与消息进行关联
         * 2.轻松批量删除条目，只要给到序号
         * 3.支持高并发 多线程
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

//        消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
//            System.out.println("确认的消息："+deliveryTag);
//            2.删除已经确认的消息，剩下就是未确认消息
            if (multiple) { // 是否批量确认
                ConcurrentNavigableMap<Long, String> confirm =
                        outstandingConfirms.headMap(deliveryTag);
                confirm.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
        };

//        开始时间
        long start = System.currentTimeMillis();
//        消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息："+deliveryTag);
        };

//        准备消息的监听器 监听哪些消息成功了，哪些消息失败了
        channel.addConfirmListener(ackCallback, nackCallback);

//        批量发送消息
        for (int i = 0; i < BATCH_SIZE; i++) {
            String message = "message" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
//            1.记录所有发送的消息的id
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + BATCH_SIZE + "条消息耗时：" + (end - start) + "ms");
        channel.close();
    }

    /**
     * 批量确认
     */
    @SneakyThrows
    public static void publishMessageBatch() {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

//        开启发布确认机制
        channel.confirmSelect();

//        开始时间
        long start = System.currentTimeMillis();

//        批量确认消息大小
        int batchSize = 100;

//        批量发布消息
        for (int i = 0; i < BATCH_SIZE; i++) {
            String message = "hello world " + i;
            channel.basicPublish("", queueName, null, message.getBytes());

//            判断达到100条的时候，批量确认
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }

//        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + BATCH_SIZE + "条消息耗时：" + (end - start) + "ms");
        channel.close();
    }

    /**
     * 单个确认
     */
    @SneakyThrows
    public static void publishMessageIndividually() {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

//        开启发布确认机制
        channel.confirmSelect();

//        开始时间
        long start = System.currentTimeMillis();
//        批量发布消息
        for (int i = 0; i < BATCH_SIZE; i++) {
            String message = "hello world " + i;
            channel.basicPublish("", queueName, null, message.getBytes());
//            单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
//        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + BATCH_SIZE + "条消息耗时：" + (end - start) + "ms");
        channel.close();
    }
}
