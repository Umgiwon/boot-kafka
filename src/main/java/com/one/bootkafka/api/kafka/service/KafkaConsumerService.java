package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.service.DeviceService;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// FIXME 카프카 컨슈머 설정 파일 및 해당 파일 수정
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DeviceService deviceService;

    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID)
    public void consume(String message) {
        log.info("kafka 수신 메시지: {}", message);

        // Kafka 에서 받은 메시지를 WebSocket을 통해 클라이언트로 전송
        deviceService.sendSocket(message);
    }
}
