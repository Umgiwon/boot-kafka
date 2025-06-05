package com.one.bootkafka.api.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.one.bootkafka.api.device.domain.dto.ParsedDeviceDataDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 디바이스 데이터를 위한 Kafka Streams 프로세서
 * KOS-500 디바이스의 원시 Modbus RTU 데이터를 처리하고 JSON 형식으로 변환
 */
@Slf4j
@Component
public class DeviceStreamProcessor {

    private static final String INPUT_TOPIC = KafkaConst.KAFKA_TOPIC; // "modbus-data"
    private static final String OUTPUT_TOPIC = KafkaConst.KAFKA_PARSED_TOPIC; // "parsed-device-data"

    // 처리 통계를 위한 카운터
    private final AtomicLong processedMessageCount = new AtomicLong(0);
    private final AtomicLong errorMessageCount = new AtomicLong(0);

    // ObjectMapper 인스턴스 (LocalDateTime 직렬화를 위한 JavaTimeModule 포함)
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ObjectMapper 초기화
     */
    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        log.info("[KafkaStreams] DeviceStreamProcessor 초기화 완료");
    }

    /**
     * 디바이스 데이터 처리를 위한 Kafka Stream 토폴로지를 생성하고 구성
     * 
     * @param builder 토폴로지를 생성하기 위한 StreamsBuilder
     * @return 구성된 KStream 인스턴스
     */
    @Bean
    public KStream<String, byte[]> deviceStreamTopology(StreamsBuilder builder) {
        // 입력 토픽에서 바이트 배열 스트림 생성
        KStream<String, byte[]> stream = builder.stream(
            INPUT_TOPIC, 
            Consumed.with(Serdes.String(), Serdes.ByteArray())
        );

        // 바이트 배열을 JSON으로 변환하고 디바이스 ID를 키로 설정
        KStream<String, String> processedStream = stream
            .map((key, value) -> {
                String jsonValue = parseBytesToJson(value);
                String newKey = extractDeviceIdFromJson(jsonValue);
                return KeyValue.pair(newKey, jsonValue);
            });

        // 처리된 스트림을 출력 토픽으로 전송
        processedStream.to(
            OUTPUT_TOPIC, 
            Produced.with(Serdes.String(), Serdes.String())
        );

        // 주기적으로 처리 통계 로깅
        processedStream.foreach((key, value) -> {
            long count = processedMessageCount.incrementAndGet();
            if (count % 100 == 0) {
                log.info("[KafkaStreams] 처리 통계 - 총 메시지: {}, 오류: {}", 
                    count, errorMessageCount.get());
            }
        });

        return stream;
    }

    /**
     * JSON 문자열에서 디바이스 ID 추출
     * 
     * @param json 파싱된 JSON 문자열
     * @return 디바이스 ID 또는 오류 시 "unknown"
     */
    private String extractDeviceIdFromJson(String json) {
        try {
            if (json.contains("error")) {
                return "error";
            }

            ParsedDeviceDataDTO dto = objectMapper.readValue(json, ParsedDeviceDataDTO.class);
            return "COMP-" + String.format("%03d", dto.getDeviceId());
        } catch (Exception e) {
            log.error("[KafkaStreams] 디바이스 ID 추출 오류: {}", e.getMessage());
            return "unknown";
        }
    }

    /**
     * Modbus RTU 프로토콜의 원시 바이트 배열 데이터를 JSON 문자열로 파싱
     * KOS-500 프로토콜 참조 가이드 기반
     * 
     * @param bytes Modbus RTU의 원시 바이트 배열
     * @return 파싱된 데이터의 JSON 문자열 표현
     */
    private String parseBytesToJson(byte[] bytes) {
        try {
            // 입력 유효성 검사
            if (bytes == null) {
                errorMessageCount.incrementAndGet();
                log.error("[KafkaStreams] null 바이트 배열 수신됨");
                return createErrorJson("null 바이트 배열 수신됨");
            }

            if (bytes.length < 8) {
                errorMessageCount.incrementAndGet();
                log.error("[KafkaStreams] 유효하지 않은 바이트 배열 길이: {}", bytes.length);
                return createErrorJson("유효하지 않은 바이트 배열 길이: " + bytes.length);
            }

            // 더 읽기 쉬운 형식으로 바이트 로깅 (디버그 레벨로 변경)
            if (log.isDebugEnabled()) {
                StringBuilder byteString = new StringBuilder();
                for (byte b : bytes) {
                    byteString.append(String.format("%02X ", b & 0xFF));
                }
                log.debug("[KafkaStreams] 바이트 파싱 중: {}", byteString.toString().trim());
            }

            // 바이트에서 DTO 생성 (비트 마스킹 수정 및 데이터 유효성 검사 추가)
            int deviceId = bytes[0] & 0xFF;
            int temperature = bytes[1] & 0xFF;
            int pressure = bytes[2] & 0xFF;
            int workingTime = bytes[3] & 0xFF;
            int deviceStatus = bytes[4] & 0xFF;

            // 디바이스 상태 유효성 검사 (0 또는 1만 유효)
            if (deviceStatus != 0 && deviceStatus != 1) {
                log.warn("[KafkaStreams] 유효하지 않은 디바이스 상태: {}, 0으로 기본값 설정", deviceStatus);
                deviceStatus = 0;
            }

            ParsedDeviceDataDTO dto = ParsedDeviceDataDTO.builder()
                    .deviceId(deviceId)
                    .temperature(temperature)
                    .pressure(pressure)
                    .workingTime(workingTime)
                    .deviceStatus(deviceStatus)
                    .reserved1(bytes[5] & 0xFF)
                    .reserved2(bytes[6] & 0xFF)
                    .reserved3(bytes[7] & 0xFF)
                    .timeStamp(LocalDateTime.now())
                    .build();

            log.debug("[KafkaStreams] 파싱된 데이터: {}", dto);

            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            errorMessageCount.incrementAndGet();
            log.error("[KafkaStreams] 파싱 오류: {}", e.getMessage(), e);
            return createErrorJson(e.getMessage());
        }
    }

    /**
     * JSON 오류 메시지 생성
     * 
     * @param errorMessage 오류 메시지
     * @return 오류 정보가 포함된 JSON 문자열
     */
    private String createErrorJson(String errorMessage) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                "error", "parse_failed",
                "message", errorMessage,
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            // 기본 형식으로 폴백
            return String.format("{\"error\":\"parse_failed\", \"message\":\"%s\", \"timestamp\":\"%s\"}", 
                    errorMessage.replace("\"", "\\\""), LocalDateTime.now());
        }
    }
}
