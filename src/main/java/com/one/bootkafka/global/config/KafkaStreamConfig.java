package com.one.bootkafka.global.config;

import com.one.bootkafka.global.constant.KafkaConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Kafka Streams 설정 클래스
 * 디바이스 데이터 스트림 처리를 위한 설정을 정의
 */
@Slf4j
@Configuration
public class KafkaStreamConfig {

    /**
     * Kafka Streams 속성 설정
     * 스트림 처리를 위한 기본 설정값들을 정의
     * 안정성과 성능을 위한 설정 포함
     *
     * @return Kafka Streams 설정 속성
     */
    @Bean(name = "deviceStreamProps")
    public Properties kafkaStreamProperties() {
        Properties props = new Properties();

        // 기본 설정
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "device-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArraySerde.class);

        // 성능 설정
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100); // 더 빠른 커밋 간격 (100ms)
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024); // 10MB 캐시
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2); // 병렬 처리를 위한 스레드 수

        // 안정성 설정
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.AT_LEAST_ONCE); // 최소 한 번 처리 보장
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);

        // 소비자 설정
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        log.info("[KafkaStreams] 스트림 설정 완료");
        return props;
    }

    /**
     * Kafka Streams 인스턴스 생성
     * 설정된 속성과 스트림 빌더를 사용하여 Kafka Streams 인스턴스를 생성
     * 스트림 상태 및 오류 처리를 위한 리스너 설정
     *
     * @param props   스트림 설정 속성
     * @param builder 스트림 토폴로지 빌더
     * @return 구성된 KafkaStreams 인스턴스
     */
    @Bean
    public KafkaStreams deviceKafkaStreams(@Qualifier("deviceStreamProps") Properties props, StreamsBuilder builder) {
        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // 스트림 상태 변경 리스너 추가
        streams.setStateListener((newState, oldState) -> {
            log.info("[KafkaStreams] 스트림 상태 변경: {} -> {}", oldState, newState);
        });

        // 스트림 오류 리스너 추가
        streams.setUncaughtExceptionHandler((thread, throwable) -> {
            log.error("[KafkaStreams] 처리되지 않은 예외 발생: {}", throwable.getMessage(), throwable);
        });

        // 애플리케이션 종료 시 스트림 정상 종료를 위한 셧다운 훅 추가
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        return streams;
    }
}
