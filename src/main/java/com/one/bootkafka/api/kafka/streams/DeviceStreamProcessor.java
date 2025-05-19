package com.one.bootkafka.api.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.bootkafka.api.device.domain.dto.ParsedDeviceDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DeviceStreamProcessor {

    private static final String INPUT_TOPIC = "raw-device-data";
    private static final String OUTPUT_TOPIC = "parsed-device-data";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public KStream<String, byte[]> deviceStreamTopology(StreamsBuilder builder) {
        KStream<String, byte[]> stream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.ByteArray())
        );

        stream.mapValues(this::parseBytesToJson)
              .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        return stream;

    }

    private String parseBytesToJson(byte[] bytes) {
        try {
            if(bytes == null || bytes.length < 8) {
                throw new IllegalArgumentException("Invalid byte array length: " + (bytes == null ? 0 : bytes.length));
            }

            log.info("[KafkaStreams] Parsing bytes : {}", bytes);

            ParsedDeviceDataDTO dto = ParsedDeviceDataDTO.builder()
                    .deviceId(bytes[0] & 0XFF)
                    .temperature(bytes[1] & 0XFF)
                    .pressure(bytes[2] & 0XFF)
                    .workingTime(bytes[3] & 0XFF)
                    .deviceStatus(bytes[4] & 0XFF)
//                    .reserved0(bytes[4] & 0XFF)
                    .reserved1(bytes[5] & 0XFF)
                    .reserved2(bytes[6] & 0XFF)
                    .reserved3(bytes[7] & 0XFF)
//                    .timeStamp(LocalDateTime.now())
                    .build();

            log.info("[KafkaStreams  2] ParsedDeviceDataDTO : {}", dto);

            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            log.error("[KafkaStreams] Parsing error : {}", e.getMessage());
//            throw new RuntimeException(e);
            return "{\"error\":\"parse_failed\"}";
        }
    }
}
