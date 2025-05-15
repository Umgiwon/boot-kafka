package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    /**
     * 카프카 수신 테스트
     * @param deviceDTO
     */
    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID, containerFactory = KafkaConst.DEVICE_INFO_FACTORY)
    public void kafkaConsumer(DeviceDTO deviceDTO) {
        log.info("kafkaConsumer - consume message : {}", deviceDTO);
    }
}
