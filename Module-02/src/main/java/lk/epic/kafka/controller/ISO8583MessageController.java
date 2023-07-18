package lk.epic.kafka.controller;

import lk.epic.kafka.dto.ISO8583FieldsDTO;
import lk.epic.kafka.producer.ISO8583MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/iso8583")
public class ISO8583MessageController {
    @Autowired
    private ISO8583MessageProducer iso8583MessageProducer;

    // This is a REST End Point
    @PostMapping("/publishISO8583Message")
    public ResponseEntity<String> publishJSONMessage(@RequestBody ISO8583FieldsDTO allFields) {
        iso8583MessageProducer.sendISO8583Message(allFields);
        return ResponseEntity.ok("All are Success..!");
    }
}
