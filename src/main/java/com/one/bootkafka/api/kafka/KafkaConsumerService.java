package com.one.bootkafka.api.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지를 보내기 위한 객체

    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "severance-comp-data", groupId = "severance-consumer-group")
    public void consume(String message) {
        log.info("kafka 수신 메시지: {}", message);

        // Kafka 에서 받은 메시지를 WebSocket을 통해 클라이언트로 전송
        messagingTemplate.convertAndSend("/topic/severance-comp-data", message);
    }
}
