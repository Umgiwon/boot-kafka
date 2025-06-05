package com.one.bootkafka.api.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.bootkafka.api.device.domain.dto.ParsedDeviceDataDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 디바이스 데이터를 위한 Kafka Streams 프로세서
 * KOS-500 디바이스의 원시 Modbus RTU 데이터를 처리하고 JSON 형식으로 변환
 */
@Slf4j
@Component
public class DeviceStreamProcessor {

    private static final String INPUT_TOPIC = KafkaConst.KAFKA_TOPIC; // "modbus-data"
    private static final String OUTPUT_TOPIC = KafkaConst.KAFKA_PARSED_TOPIC; // "parsed-device-data"

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 디바이스 데이터 처리를 위한 Kafka Stream 토폴로지를 생성하고 구성
     * 
     * @param builder 토폴로지를 생성하기 위한 StreamsBuilder
     * @return 구성된 KStream 인스턴스
     */
    @Bean
    public KStream<String, byte[]> deviceStreamTopology(StreamsBuilder builder) {
        KStream<String, byte[]> stream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.ByteArray()));

        stream.mapValues(this::parseBytesToJson)
              .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        return stream;
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
                log.error("[KafkaStreams] null 바이트 배열 수신됨");
                return createErrorJson("null 바이트 배열 수신됨");
            }

            if (bytes.length < 8) {
                log.error("[KafkaStreams] 유효하지 않은 바이트 배열 길이: {}", bytes.length);
                return createErrorJson("유효하지 않은 바이트 배열 길이: " + bytes.length);
            }

            // 더 읽기 쉬운 형식으로 바이트 로깅
            StringBuilder byteString = new StringBuilder();
            for (byte b : bytes) {
                byteString.append(String.format("%02X ", b & 0xFF));
            }
            log.info("[KafkaStreams] 바이트 파싱 중: {}", byteString.toString().trim());

            // 바이트에서 DTO 생성
            ParsedDeviceDataDTO dto = ParsedDeviceDataDTO.builder()
                    .deviceId(bytes[0] & 0XFF)
                    .temperature(bytes[1] & 0XFF)
                    .pressure(bytes[2] & 0XFF)
                    .workingTime(bytes[3] & 0XFF)
                    .deviceStatus(bytes[4] & 0XFF)
                    .reserved1(bytes[5] & 0XFF)
                    .reserved2(bytes[6] & 0XFF)
                    .reserved3(bytes[7] & 0XFF)
                    .timeStamp(LocalDateTime.now())
                    .build();

            log.info("[KafkaStreams] 파싱된 데이터: {}", dto);

            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
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
        return String.format("{\"error\":\"parse_failed\", \"message\":\"%s\", \"timestamp\":\"%s\"}", 
                errorMessage, LocalDateTime.now());
    }
}
