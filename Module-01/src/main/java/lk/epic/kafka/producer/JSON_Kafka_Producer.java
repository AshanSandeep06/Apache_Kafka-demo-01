package lk.epic.kafka.producer;

import lk.epic.kafka.pojo.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class JSON_Kafka_Producer {
    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(JSON_Kafka_Producer.class);

    // To Produce JSON message and send it to Kafka Topic
    public void sendJSONMessage(Customer customer) {
        // Log the customer object
        LOGGER.info(String.format("Message Sent -> %s", customer.toString()));

        // Creating a Message
        Message<Customer> message = MessageBuilder.withPayload(customer)
                .setHeader(KafkaHeaders.TOPIC, "myTopic").build();

        kafkaTemplate.send(message);
    }
}
