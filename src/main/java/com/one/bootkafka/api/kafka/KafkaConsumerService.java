package com.one.bootkafka.api.kafka;

import com.one.bootkafka.global.config.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerService {

    private final WebSocketHandler webSocketHandler;

    @KafkaListener(topics = "severance-comp-data", groupId = "severance-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();
        log.info("kafka 수신 메시지: {}", message);

        // WebSocket 클라이언트로 전송
        webSocketHandler.broadcast(message);
    }
}
