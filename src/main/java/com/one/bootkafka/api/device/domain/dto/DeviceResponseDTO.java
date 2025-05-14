package com.one.bootkafka.api.device.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.one.bootkafka.global.enums.device.DeviceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Device 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class DeviceResponseDTO implements Serializable {

    @Schema(description = "기기 Id")
    private String deviceId;

    @Schema(description = "온도")
    private Float temperature;

    @Schema(description = "압력")
    private Float pressure;

    @Schema(description = "작동시간")
    private Float workingTime;

    @Schema(description = "기기상태")
    private DeviceStatus deviceStatus;

    private LocalDateTime timeStamp;
}
