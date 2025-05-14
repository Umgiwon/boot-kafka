package com.one.bootkafka.api.device.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    // WebSocket 메시지를 보내기 위한 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    public void sendSocket(String message) {

        // TODO message -> dto로 데이터 가공 및 저장


        // Kafka 에서 받은 메시지를 WebSocket을 통해 클라이언트로 전송
        messagingTemplate.convertAndSend("/sub/severance-comp-data", message);
    }
}
