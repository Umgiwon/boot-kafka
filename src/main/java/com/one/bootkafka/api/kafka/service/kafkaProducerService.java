package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

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
     * 비동기 콜백을 통해 전송 결과를 처리
     *
     * @param deviceDTO 전송할 디바이스 데이터
     * @return 전송된 동일한 deviceDTO
     */
    public DeviceDTO kafkaProducer(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            log.error("[KafkaProducer] 전송할 deviceDTO가 null입니다");
            throw new IllegalArgumentException("deviceDTO cannot be null");
        }

        // 메시지 키로 deviceId를 사용하여 동일한 디바이스의 메시지가 같은 파티션으로 전송되도록 함
        String messageKey = deviceDTO.getDeviceId();

        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KAFKA_TOPIC, messageKey, deviceDTO);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                // 성공 처리
                log.info("[KafkaProducer] 메시지 전송 성공: 토픽={}, 파티션={}, 오프셋={}, 키={}, 데이터={}",
                    KAFKA_TOPIC,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    messageKey,
                    deviceDTO);
            } else {
                // 실패 처리
                log.error("[KafkaProducer] 메시지 전송 실패: 토픽={}, 키={}, 데이터={}, 오류={}",
                    KAFKA_TOPIC, messageKey, deviceDTO, ex.getMessage(), ex);
            }
        });

        return deviceDTO;
    }
}
