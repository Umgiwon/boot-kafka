package com.one.bootkafka.global.config;

import com.one.bootkafka.global.constant.KafkaConst;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Kafka Streams 설정 클래스
 * 디바이스 데이터 스트림 처리를 위한 설정을 정의
 */
@Configuration
public class KafkaStreamConfig {

    /**
     * Kafka Streams 속성 설정
     * 스트림 처리를 위한 기본 설정값들을 정의
     * 
     * @return Kafka Streams 설정 속성
     */
    @Bean(name = "deviceStreamProps")
    public Properties kafkaStreamProperties() {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "device-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArraySerde.class);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);

        return props;
    }

    /**
     * Kafka Streams 인스턴스 생성
     * 설정된 속성과 스트림 빌더를 사용하여 Kafka Streams 인스턴스를 생성
     * 
     * @param props 스트림 설정 속성
     * @param builder 스트림 토폴로지 빌더
     * @return 구성된 KafkaStreams 인스턴스
     */
    @Bean
    public KafkaStreams deviceKafkaStreams(@Qualifier("deviceStreamProps") Properties props, StreamsBuilder builder) {
        return new KafkaStreams(builder.build(), props);
    }
}
