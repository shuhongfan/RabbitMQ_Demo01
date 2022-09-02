package com.shf.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 回调接口
 */
@Slf4j
@Configuration
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);    //  注入
        rabbitTemplate.setReturnsCallback(this);    //  注入
    }

    /**
     * 交换机确认回调方法
     * 1. 发消息 交换机收到了 回调
     * 1.1 correlationData 保存回调信息的ID及相关信息
     * 1.2 交换机收到消息
     * 1.3 cause null
     * <p>
     * 2. 发消息 交换机接收失败了 回调
     * 2.1 correlationData 保存回调信息的ID及相关信息
     * 2.2 交换机收到信息 ack=false
     * 2.3 cause 失败的原因
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("消息发送成功，id：{}", id);
        } else {
            log.info("消息发送失败，id：{}，原因：{}", id, cause);
        }
    }

    /**
     * 当消息无法路由的时候的回调方法
     * @param message the returned message and metadata.
     */
    @Override
    public void returnedMessage(ReturnedMessage message) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因 :{}, 路 由 key:{}",
                message.getMessage(),
                message.getExchange(),
                message.getReplyText(),
                message.getRoutingKey());
    }

}
