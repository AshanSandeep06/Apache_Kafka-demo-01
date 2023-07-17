package lk.epic.kafka_zooKeeper.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

// To send messages to the Topic we are going to
// use Spring Provided Kafka Template

// We have created Kafka Producer which will use Kafka Template
// to send messages to the topic
@Service
public class Kafka_Producer {
    //Let's inject the Kafka template in this Spring Bean
    private KafkaTemplate<String, String> kafkaTemplate;

    public Kafka_Producer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Put a log statement in order to print this message
    // Then can use, Spring-boot provided Default Logger
    // To log the message
    // Logger Instance to log this message
    private static final Logger LOGGER = LoggerFactory.getLogger(Kafka_Producer.class);

    public void sendMessages(String message) {
        LOGGER.info(String.format("Message sent %s", message));
        kafkaTemplate.send("topic-1", message);
    }
}
