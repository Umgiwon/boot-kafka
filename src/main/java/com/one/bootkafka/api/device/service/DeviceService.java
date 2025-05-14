package com.one.bootkafka.api.device.service;

import com.one.bootkafka.api.device.domain.dto.DeviceResponseDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceService {

    // WebSocket 메시지를 보내기 위한 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Device 데이터 수신
     * @param deviceResponseDTO Device 데이터 객체
     */
    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID)
    public void sendSocket(DeviceResponseDTO deviceResponseDTO) {
        log.info("DeviceService.sendSocket() : {}", deviceResponseDTO);
        // TODO message -> dto로 데이터 가공 및 저장


        // Kafka 에서 받은 메시지를 WebSocket을 통해 클라이언트로 전송
        messagingTemplate.convertAndSend("/sub/severance-data", deviceResponseDTO);
    }
}
