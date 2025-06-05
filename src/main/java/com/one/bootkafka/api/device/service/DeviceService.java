package com.one.bootkafka.api.device.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 디바이스 관련 작업을 위한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceService {

    // WebSocket 메시징 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    // 디바이스 데이터를 위한 WebSocket 토픽
    private static final String DEVICE_DATA_TOPIC = "/sub/device-data";

    /**
     * WebSocket을 통해 클라이언트에게 디바이스 데이터 전송
     * 
     * @param deviceDTO 전송할 디바이스 데이터
     */
    public void sendSocket(DeviceDTO deviceDTO) {
        try {
            log.info("[WebSocket] 디바이스 데이터 전송 중: {}", deviceDTO);
            messagingTemplate.convertAndSend(DEVICE_DATA_TOPIC, deviceDTO);
            log.debug("[WebSocket] 디바이스 데이터 전송 완료: {}", deviceDTO.getDeviceId());
        } catch (Exception e) {
            log.error("[WebSocket] 디바이스 데이터 전송 중 오류 발생: {}", e.getMessage(), e);
            // 오류가 발생해도 애플리케이션의 다른 부분에 영향을 주지 않도록 예외를 여기서 처리
        }
    }
}
