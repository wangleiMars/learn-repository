package com.mars.rocketmq.learn;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AveragelyTest {

    public static void main(String[] args) {
        String consumerGroup="111";
        String currentCID="macbook3";

        List<MessageQueue> mqAll = new ArrayList<>();
        for (int i =1 ;i<=10;i++){
            MessageQueue queue = new MessageQueue();
            queue.setTopic("Topic-test");
            queue.setBrokerName("BrokerName-test");
            queue.setQueueId(i);
            mqAll.add(queue);
        }
        List<String> cidAll = new ArrayList<>();
        cidAll.add("macbook1");
        cidAll.add("macbook2");
        cidAll.add("macbook3");
        System.out.println(allocate(consumerGroup,currentCID,mqAll,cidAll));
    }


    public static List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> mqAll, List<String> cidAll) {
        if (currentCID == null || currentCID.length() < 1) {
            throw new IllegalArgumentException("currentCID is empty");
        }
        if (mqAll == null || mqAll.isEmpty()) {
            throw new IllegalArgumentException("mqAll is null or mqAll empty");
        }
        if (cidAll == null || cidAll.isEmpty()) {
            throw new IllegalArgumentException("cidAll is null or cidAll empty");
        }

        List<MessageQueue> result = new ArrayList<MessageQueue>();
        if (!cidAll.contains(currentCID)) {
            log.info("[BUG] ConsumerGroup: {} The consumerId: {} not in cidAll: {}",
                    consumerGroup,
                    currentCID,
                    cidAll);
            return result;
        }

        int index = cidAll.indexOf(currentCID);
        int mod = mqAll.size() % cidAll.size();
        int averageSize = mqAll.size() <= cidAll.size() ? 1 :
                (mod > 0 && index < mod ? mqAll.size() / cidAll.size() + 1 :
                        mqAll.size() / cidAll.size());
        int startIndex = (mod > 0 && index < mod) ?
                index * averageSize :
                index * averageSize + mod;
        int range = Math.min(averageSize, mqAll.size() - startIndex);
        for (int i = 0; i < range; i++) {
            result.add(mqAll.get((startIndex + i) % mqAll.size()));
        }
        return result;
    }
}
