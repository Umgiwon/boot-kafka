package com.one.bootkafka.api.device.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.one.bootkafka.global.enums.device.DeviceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Device 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class DeviceDTO implements Serializable {

    @Schema(description = "기기 Id", example = "COMP-001")
    private String deviceId;

    @Schema(description = "온도", example = "72.5")
    private BigDecimal temperature;

    @Schema(description = "압력", example = "1.1")
    private BigDecimal pressure;

    @Schema(description = "작동시간", example = "1543.2")
    private BigDecimal workingTime;

    @Schema(description = "기기상태", example = "RUNNING")
    private DeviceStatus deviceStatus;

    @Schema(description = "시간", example = "2025-05-15T14:35:00")
    private LocalDateTime timeStamp;
}
