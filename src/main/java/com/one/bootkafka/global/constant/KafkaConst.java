package com.one.bootkafka.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Kafka 관련 상수 정의 클래스
 * 토픽, 그룹 ID, 브로커 서버 등의 상수를 정의
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자
public class KafkaConst {

    // Kafka 기본 설정
    public static final String KAFKA_GROUP_ID = "comp";
    public static final String KAFKA_BROKER_SERVER = "192.168.219.217:9092";

    // Kafka 토픽
    public static final String KAFKA_TOPIC = "modbus-data";
    public static final String KAFKA_PARSED_TOPIC = "parsed-device-data";

    // Kafka 리스너 컨테이너 팩토리
    public static final String DEVICE_INFO_FACTORY = "kafkaListenerContainerFactory";
    public static final String PARSED_DEVICE_INFO_FACTORY = "parsedDataListenerContainerFactory";
}
