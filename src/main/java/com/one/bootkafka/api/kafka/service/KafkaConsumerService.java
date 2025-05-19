package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.api.device.service.DeviceService;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DeviceService deviceService;

    /**
     * 카프카 수신 테스트
     * @param deviceDTO
     */
    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID, containerFactory = KafkaConst.DEVICE_INFO_FACTORY)
    public void kafkaConsumer(DeviceDTO deviceDTO) {

        log.info("kafkaConsumer - consume message : {}", deviceDTO);

        // WebSocket 전송
        deviceService.sendSocket(deviceDTO);
    }

    @KafkaListener(topics = KafkaConst.KAFKA_PARSED_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID, containerFactory = KafkaConst.DEVICE_INFO_FACTORY)
    public void consume(DeviceDTO deviceDTO) {

        log.info("consume - consume message : {}", deviceDTO);

        // WebSocket 전송
        deviceService.sendSocket(deviceDTO);
    }
}
