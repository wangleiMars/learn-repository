package com.mars.rocketmq.learn.consumer;

import com.mars.rocketmq.learn.bean.MqMessage;
import com.mars.rocketmq.learn.common.CommonTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = CommonTopic.common_topic,
        consumerGroup = "common_consumer_a_group2")
public class MqMessageConsumer2 implements RocketMQListener<MqMessage> {
    @Override
    public void onMessage(MqMessage message) {
        log.info("{}收到消息：{}", this.getClass().getSimpleName(), message);
    }
}
