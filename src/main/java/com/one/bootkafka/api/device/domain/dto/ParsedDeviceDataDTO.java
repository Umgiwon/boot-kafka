package com.one.bootkafka.api.device.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ParsedDeviceDataDTO implements Serializable {

    @Schema(description = "기기 Id", example = "COMP-001")
    private Integer deviceId;

    @Schema(description = "온도", example = "72.5")
    private Integer temperature;

    @Schema(description = "압력", example = "1.1")
    private Integer pressure;

    @Schema(description = "작동시간", example = "1543.2")
    private Integer workingTime;

    @Schema(description = "기기상태", example = "RUNNING")
    private Integer deviceStatus;

    @Schema(description = "예약어1", example = "72.5")
    private Integer reserved1;

    @Schema(description = "예약어2", example = "72.5")
    private Integer reserved2;

    @Schema(description = "예약어3", example = "72.5")
    private Integer reserved3;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "시간", example = "2025-05-15T14:35:00")
    private LocalDateTime timeStamp;
}
