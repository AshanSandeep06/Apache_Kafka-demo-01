package lk.epic.kafka.controller;

import lk.epic.kafka.pojo.Customer;
import lk.epic.kafka.producer.JSON_Kafka_Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jsonKafka")
public class JSONMessageController {
    @Autowired
    private JSON_Kafka_Producer jsonKafkaProducer;

    // This is a REST End Point
    @PostMapping("/publishJsonMsg")
    public ResponseEntity<String> publishJSONMessage(@RequestBody Customer customer){
        jsonKafkaProducer.sendJSONMessage(customer);
        return ResponseEntity.ok("JSON Message sent to Kafka Topic..!");
    }
}
