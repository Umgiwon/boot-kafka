package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.one.bootkafka.global.constant.KafkaConst.KAFKA_TOPIC;

/**
 * Kafka 토픽에 메시지를 생성하는 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Kafka 토픽으로 디바이스 데이터 전송
     *
     * @param deviceDTO 전송할 디바이스 데이터
     * @return 전송된 동일한 deviceDTO
     */
    public DeviceDTO kafkaProducer(DeviceDTO deviceDTO) {
        kafkaTemplate.send(KAFKA_TOPIC, deviceDTO);
        log.info("[KafkaProducer] 토픽으로 메시지 전송: {}, 데이터: {}", KAFKA_TOPIC, deviceDTO);
        return deviceDTO;
    }
}
