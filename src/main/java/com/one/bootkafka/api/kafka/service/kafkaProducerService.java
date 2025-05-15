package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class kafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 카프카 전송 테스트
     * @param topic
     * @param deviceDTO
     * @return
     */
    public DeviceDTO kafkaProducer(String topic, DeviceDTO deviceDTO) {
        kafkaTemplate.send(topic, deviceDTO);
        log.info("kafkaProducer - topic : {}, message : {}", topic, deviceDTO);
        return deviceDTO;
    }
}
