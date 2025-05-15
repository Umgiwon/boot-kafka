package com.one.bootkafka.api.device.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * WebSocket을 통해 클라이언트로 전송
     * @param deviceDTO
     */
    public void sendSocket(DeviceDTO deviceDTO) {

        messagingTemplate.convertAndSend("/sub/severance-data", deviceDTO);
    }
}
