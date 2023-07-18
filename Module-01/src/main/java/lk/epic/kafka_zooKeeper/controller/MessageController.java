package lk.epic.kafka_zooKeeper.controller;

import lk.epic.kafka_zooKeeper.kafka.Kafka_Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messageController")
@CrossOrigin
public class MessageController {
    // We need to inject Kafka Producer
    private Kafka_Producer kafkaProducer;

    public MessageController(Kafka_Producer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/publish")
    //RequestParam annotation = is used for, Query Parameters
    public ResponseEntity<String> publish(@RequestParam("message") String message) {
        kafkaProducer.sendMessages(message);
        return ResponseEntity.ok("Message sent to the Topic.");
    }
}
