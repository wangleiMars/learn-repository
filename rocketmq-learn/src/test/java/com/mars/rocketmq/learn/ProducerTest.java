package com.mars.rocketmq.learn;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProducerTest {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamespace("xxxxx:10911");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        Message message = new Message("test_topic", "tagA", "orderId", "hello world".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = defaultMQProducer.send(message);

        System.out.printf("%s%n", sendResult);

        defaultMQProducer.shutdown();
    }

    public void sendMsgOrderly() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamespace("xxxxx:10911");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        Message message = new Message("test_topic", "tagA", "orderId", "hello world".getBytes(StandardCharsets.UTF_8));
        message.setDelayTimeLevel(4);
        int hashKey = 123;
        SendResult sendResult = defaultMQProducer.send(message,
                (List<MessageQueue> mqs, Message msg, Object o) -> {
                    int id = (int) o;
                    int index = id % mqs.size();
                    return mqs.get(index);
                },
                hashKey);

        System.out.printf("%s%n", sendResult);

        defaultMQProducer.shutdown();
    }

    public void sendTransaction() throws MQClientException {
        // 初始化事务消费生产者
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("TransactionMQProducer");
        transactionMQProducer.setCheckThreadPoolMaxSize(2);
        transactionMQProducer.setCheckThreadPoolMinSize(1);
        transactionMQProducer.setCheckRequestHoldMax(2000);
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 本地事务处理
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // 回查事务
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        Message message = new Message("test_topic", "tagA", "orderId", "hello world".getBytes(StandardCharsets.UTF_8));
        transactionMQProducer.sendMessageInTransaction(message,null);
        transactionMQProducer.start();
    }

    public void sendOneWay()throws Exception{
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamespace("xxxxx:10911");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        Message message = new Message("test_topic", "tagA", "orderId", "hello world".getBytes(StandardCharsets.UTF_8));
        defaultMQProducer.sendOneway(message);

        defaultMQProducer.shutdown();
    }

    public void sendList()throws Exception{
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("DefaultMQProducer");
        defaultMQProducer.setNamespace("xxxxx:10911");
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
        defaultMQProducer.start();
        List<Message> list = new ArrayList();
        Message message = new Message("test_topic", "tagA", "orderId", "hello world".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_topic", "tagA", "orderId", "hello world2".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        message = new Message("test_topic", "tagA", "orderId", "hello world3".getBytes(StandardCharsets.UTF_8));
        list.add(message);
        defaultMQProducer.sendOneway(message);

        defaultMQProducer.shutdown();
    }
}
