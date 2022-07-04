package com.mars.rocketmq.learn;

import com.mars.rocketmq.learn.bean.MqMessage;
import com.mars.rocketmq.learn.common.CommonTopic;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.lang.management.ManagementFactory;
import java.util.UUID;

@SpringBootTest
class RocketmqLearnApplicationTests {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @Test
    void contextLoads() {
        for (int i = 0; i < 6; i++) {


            MqMessage mqMessage = MqMessage
                    .builder().name("普通消息" + i)
                    .msg("这是普通消息" + i).build();

            Message<MqMessage> message = MessageBuilder.withPayload(mqMessage).build();
            SendResult sendResult = rocketMQTemplate.syncSend(CommonTopic.common_topic, message);
        }
    }

    @Test
    public void transactionSend() {
        String transactionId = UUID.randomUUID().toString();
        //如果可以删除订单则发送消息给rocketmq，让用户中心消费消息
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("add-amount",
                MessageBuilder.withPayload(
                                "test"
                        )
                        .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                        .setHeader("order_id", "212121")
                        .build()
                , "123jjjj"
        );

        System.out.println(transactionSendResult);
    }

    @Test
    public void getName(){
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
    }

}
