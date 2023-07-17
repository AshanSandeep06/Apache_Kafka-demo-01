package lk.epic.kafka_zooKeeper.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// Basically we should create a Spring Bean from this class
// To Configure the Kafka Topic
@Configuration
public class KafkaTopicConfig {
    // We are going to create a Spring Bean
    // To create a Kafka Topic
    @Bean
    public NewTopic demoTopic() {
        //Give a name to the Topic
        return TopicBuilder.name("topic01").build();
    }
}
