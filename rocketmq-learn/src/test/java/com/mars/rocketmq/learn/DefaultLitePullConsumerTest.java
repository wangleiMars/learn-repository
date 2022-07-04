package com.mars.rocketmq.learn;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class DefaultLitePullConsumerTest {
    public static void main(String[] args) throws Exception {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "warn");


        sendList();
        log.info("初始化消息完毕");


        DefaultLitePullConsumer defaultMQPullConsumer = new DefaultLitePullConsumer();
        defaultMQPullConsumer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQPullConsumer.setConsumerGroup("testconsumergroup");

        defaultMQPullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPullConsumer.subscribe("test_pull_consumer", "*");
        defaultMQPullConsumer.setAutoCommit(true); //该值默认为 true

        defaultMQPullConsumer.setPullBatchSize(2);

        defaultMQPullConsumer.start();
        int i=1;
        while (true){
            log.info("第{}次",i);
            List<MessageExt> list =  defaultMQPullConsumer.poll();
            for (MessageExt messageExt : list) {
                log.info("messageExt:{}",messageExt.toString());
            }
            i++;
        }


    }


    public static void sendList() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        List<Message> list = new ArrayList();
        Message message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"), "hello world1".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"), "hello world2".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"), "hello world3".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        log.info("发送结果:{}", defaultMQProducer.send(message).toString());

        defaultMQProducer.shutdown();
    }
}
