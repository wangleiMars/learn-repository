package com.mars.rocketmq.learn.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RocketMQMessageListener(topic = "add-amount", consumerGroup = "cloud-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionConsumerListener implements RocketMQListener<String> {
    /**
     * 收到消息的业务逻辑
     */
    @Override
    public void onMessage(String userAddMoneyDTO) {
        log.info("received message: {}", userAddMoneyDTO);
        log.info("add money success");
    }
}
