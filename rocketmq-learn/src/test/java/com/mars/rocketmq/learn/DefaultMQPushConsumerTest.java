package com.mars.rocketmq.learn;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DefaultMQPushConsumerTest {
    public static void main(String[] args) throws Exception {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "warn");

        sendList();
        log.info("初始化消息完毕");


        DefaultMQPushConsumer defaultMQPullConsumer = new DefaultMQPushConsumer();
        defaultMQPullConsumer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQPullConsumer.setConsumerGroup("testconsumergroup");
        defaultMQPullConsumer.subscribe("test_pull_consumer","*");
        AtomicInteger integer = new AtomicInteger();
        defaultMQPullConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                int am =  integer.addAndGet(1);
                ConsumeConcurrentlyStatus consumeConcurrentlyStatus= ConsumeConcurrentlyStatus.RECONSUME_LATER;
                if(am%2!=0){
                    consumeConcurrentlyStatus =  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                log.info("msgs.size:{},consumeConcurrentlyStatus:{},am:{}",msgs.size(),consumeConcurrentlyStatus.toString(),am);
                return consumeConcurrentlyStatus;
            }
        });

        defaultMQPullConsumer.start();




//        defaultMQPullConsumer.shutdown();

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
