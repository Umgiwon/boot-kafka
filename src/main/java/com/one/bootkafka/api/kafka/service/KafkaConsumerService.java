package com.one.bootkafka.api.kafka.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.api.device.domain.dto.ParsedDeviceDataDTO;
import com.one.bootkafka.api.device.service.DeviceService;
import com.one.bootkafka.api.device.service.DeviceServiceTx;
import com.one.bootkafka.global.constant.KafkaConst;
import com.one.bootkafka.global.enums.device.DeviceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Kafka 메시지 소비 서비스
 * 원시 디바이스 데이터와 파싱된 디바이스 데이터를 소비하고 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DeviceService deviceService;
    private final DeviceServiceTx deviceServiceTx;

    /**
     * Kafka에서 원시 디바이스 데이터를 소비하고, 데이터베이스에 저장하며, WebSocket으로 전송
     * 
     * @param deviceDTO Kafka에서 수신한 원시 디바이스 데이터
     */
    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC, groupId = KafkaConst.KAFKA_GROUP_ID, containerFactory = KafkaConst.DEVICE_INFO_FACTORY)
    public void kafkaConsumer(DeviceDTO deviceDTO) {
        try {
            log.info("[KafkaConsumer] 원시 데이터 수신: {}", deviceDTO);

            // 데이터 유효성 검사
            if (deviceDTO == null) {
                log.error("[KafkaConsumer] null deviceDTO 수신됨");
                return;
            }

            // 데이터베이스에 저장
            deviceServiceTx.saveDeviceData(deviceDTO);

            // WebSocket으로 전송
            deviceService.sendSocket(deviceDTO);
        } catch (Exception e) {
            log.error("[KafkaConsumer] 원시 데이터 처리 오류: {}", e.getMessage(), e);
        }
    }

    /**
     * Kafka에서 파싱된 디바이스 데이터를 소비하고, 데이터베이스에 저장하며, WebSocket으로 전송
     * 
     * @param parsedData Kafka에서 수신한 파싱된 디바이스 데이터
     */
    @KafkaListener(
        topics = KafkaConst.KAFKA_PARSED_TOPIC, 
        groupId = KafkaConst.KAFKA_GROUP_ID, 
        containerFactory = KafkaConst.PARSED_DEVICE_INFO_FACTORY
    )
    public void consume(ParsedDeviceDataDTO parsedData) {
        try {
            log.info("[KafkaConsumer] 파싱된 데이터 수신: {}", parsedData);

            // 데이터 유효성 검사
            if (parsedData == null) {
                log.error("[KafkaConsumer] null parsedData 수신됨");
                return;
            }

            // ParsedDeviceDataDTO를 DeviceDTO로 변환
            DeviceDTO deviceDTO = convertToDeviceDTO(parsedData);

            log.info("[KafkaConsumer] DeviceDTO로 변환됨: {}", deviceDTO);

            // 데이터베이스에 저장
            deviceServiceTx.saveDeviceData(deviceDTO);

            // WebSocket으로 전송
            deviceService.sendSocket(deviceDTO);
        } catch (Exception e) {
            log.error("[KafkaConsumer] 파싱된 데이터 처리 오류: {}", e.getMessage(), e);
        }
    }

    /**
     * ParsedDeviceDataDTO를 DeviceDTO로 변환
     * 정수 값을 적절한 스케일의 BigDecimal로 변환하여 정밀도 유지
     * 
     * @param parsedData 파싱된 디바이스 데이터
     * @return DeviceDTO 객체
     */
    private DeviceDTO convertToDeviceDTO(ParsedDeviceDataDTO parsedData) {
        // 온도: 정수 값을 0.1 단위로 변환 (예: 72 -> 7.2)
        BigDecimal temperature = new BigDecimal(parsedData.getTemperature())
                .divide(BigDecimal.TEN, 1, RoundingMode.HALF_UP);

        // 압력: 정수 값을 0.01 단위로 변환 (예: 110 -> 1.10)
        BigDecimal pressure = new BigDecimal(parsedData.getPressure())
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

        // 작동시간: 정수 값을 1 단위로 유지
        BigDecimal workingTime = new BigDecimal(parsedData.getWorkingTime());

        return DeviceDTO.builder()
                .deviceId("COMP-" + String.format("%03d", parsedData.getDeviceId()))
                .temperature(temperature)
                .pressure(pressure)
                .workingTime(workingTime)
                .deviceStatus(parsedData.getDeviceStatus() == 1 ? DeviceStatus.RUNNING : DeviceStatus.STOP)
                .timeStamp(parsedData.getTimeStamp())
                .build();
    }
}
