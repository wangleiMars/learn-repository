package com.mars.rocketmq.learn;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class DefaultMQPullConsumerTest {


    public static void main(String[] args) throws Exception {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "warn");

//        sendList();
        log.info("初始化消息完毕");


        DefaultMQPullConsumer defaultMQPullConsumer = new DefaultMQPullConsumer();
        defaultMQPullConsumer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQPullConsumer.setConsumerGroup("testconsumergroup");
        defaultMQPullConsumer.start();


        /**
         * 获取一个Topic的全部Queue信息。
         */
        Set<MessageQueue> listMessageQueue = defaultMQPullConsumer.fetchSubscribeMessageQueues("test_pull_consumer");

        for (MessageQueue messageQueue : listMessageQueue) {
            log.info("messageQueue:{}", messageQueue);
            MessageQueue mq = new MessageQueue();
            mq.setTopic("test_pull_consumer");
            mq.setBrokerName(messageQueue.getBrokerName());
            mq.setQueueId(messageQueue.getQueueId());

            long offset = 0l;
            int maxNums = 10;

            PullResult pullResult = defaultMQPullConsumer.pull(mq, "*", offset, maxNums);
            log.info("pullResult:{}", pullResult);
            if (null != pullResult.getMsgFoundList() && pullResult.getMsgFoundList().size() > 0) {
                for (MessageExt messageExt : pullResult.getMsgFoundList()) {
                    log.info("messageExt:{}",messageExt);
                    // 放入重试队列
                    defaultMQPullConsumer.sendMessageBack(messageExt, 3);

                }
            }
        }

        defaultMQPullConsumer.shutdown();

    }


    public static void sendList() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        List<Message> list = new ArrayList();
        Message message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"), "hello world1".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"), "hello world2".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_pull_consumer", "tagA", DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"), "hello world3".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        log.info("发送结果:{}", defaultMQProducer.send(message).toString());

        defaultMQProducer.shutdown();
    }

}
