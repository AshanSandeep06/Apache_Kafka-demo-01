package lk.epic.kafka.controller;

import lk.epic.kafka.producer.String_Kafka_Producer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messageController")
@CrossOrigin
public class MessageController {
    // We need to inject Kafka Producer
    private String_Kafka_Producer kafkaProducer;

    public MessageController(String_Kafka_Producer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/publish")
    //RequestParam annotation = is used for, Query Parameters
    public ResponseEntity<String> publish(@RequestParam("message") String message) {
        kafkaProducer.sendMessages(message);
        return ResponseEntity.ok("Message sent to the Topic.");
    }
}
