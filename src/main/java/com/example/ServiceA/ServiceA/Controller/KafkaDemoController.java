package com.example.ServiceA.ServiceA.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KafkaDemoController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/v1/kafka/send")
    public ResponseEntity<?> sendMessageToKafka(
            @RequestBody Map<String, String> payload) {
        try {
            String topic = payload.get("topic");
            String message = payload.get("message");

                if (topic == null || topic.isEmpty()) {
                    return ResponseEntity.badRequest().body("Topic is missing in the request body.");
                }

            if (message == null || message.isEmpty()) {
                return ResponseEntity.badRequest().body("Message is missing in the request body.");
            }
            kafkaTemplate.send(topic, message).get();
            return ResponseEntity.ok(Map.of("result","Message sent to Kafka topic: " + topic));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("result", "Failed to send message to Kafka: " + e.getMessage()));
        }
    }
}
