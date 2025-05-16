package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.one.bootkafka.global.constant.KafkaConst.KAFKA_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class kafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 카프카 전송 테스트
     * @param deviceDTO
     * @return
     */
    public DeviceDTO kafkaProducer(DeviceDTO deviceDTO) {
        kafkaTemplate.send(KAFKA_TOPIC, deviceDTO);
        log.info("kafkaProducer - topic : {}, message : {}", KAFKA_TOPIC, deviceDTO);
        return deviceDTO;
    }
}
