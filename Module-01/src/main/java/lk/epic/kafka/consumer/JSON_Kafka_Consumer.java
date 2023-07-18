package lk.epic.kafka.consumer;

import lk.epic.kafka.pojo.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JSON_Kafka_Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSON_Kafka_Consumer.class);

    @KafkaListener(topics = "myTopic", groupId = "myGroup")
    public void consumeJSONMessage(Customer customer){
        System.out.println("JSON Kafka Consumer");
        LOGGER.info(String.format("JSON Message was Received -> %s", customer.toString()));
    }
}
