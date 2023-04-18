package com.sport.notificationchannelmanager.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class KafkaCustomConsumer {

    public ConsumerRecord<String, String> getLastMessage(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "${spring.kafka.bootstrap-servers}:9092");
        props.put("group.id", "${spring.kafka.consumer.group-id}");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // assign topic partition to the consumer
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        List<TopicPartition> topicPartitionsList = List.of(topicPartition);
        consumer.assign(topicPartitionsList);

        // seek to end of the topic partition
        consumer.seekToEnd(topicPartitionsList);

        // get the last record
        ConsumerRecords<String, String> records = consumer.poll(1000);
        if (!records.isEmpty()) {
            return records.records(topicPartition).get(records.count() - 1);
        }
        return null;
    }
}
