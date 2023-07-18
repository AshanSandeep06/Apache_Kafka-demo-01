package lk.epic.kafka_zooKeeper.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
//Specify should create a Spring Bean from this class
public class Kafka_Consumer {
    // We need to create a Subscriber Method which will subscribe
    // to the Topic

    private static final Logger LOGGER = LoggerFactory.getLogger(Kafka_Consumer.class);

    // @KafkaListener annotation is used to, Subscribe to the Topic
    // Consume Subscribers/Consume Subscriber methods
    // We also should provide a Consumer Group id to this annotation
    @KafkaListener(topics = "myTopic", groupId = "myGroup")
    public void consumeMessage(String message) {
        LOGGER.info(String.format("Message Received -> %s", message));
    }
}
