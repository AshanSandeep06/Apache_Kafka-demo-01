package lk.epic.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic newTopic() {
        System.out.println("Topic is configured");
        return TopicBuilder.name("isoTopic").build();
    }

    @Bean
    public NewTopic newTopic2() {
        return TopicBuilder.name("isoResponseTopic").build();
    }
}
