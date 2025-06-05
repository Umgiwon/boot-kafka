package com.one.bootkafka.api.device.domain.entity;

import com.one.bootkafka.global.domain.entity.BaseEntity;
import com.one.bootkafka.global.enums.device.DeviceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_DEVICE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVICE_SN", updatable = false, nullable = false)
    @Comment("기기 순번")
    private Long deviceSn;

    @Column(name = "DEVICE_ID", length = 20)
    @Comment("기기 ID")
    private String deviceId;

    @Column(name = "TEMPERATURE", nullable = false)
    @Comment("온도")
    private BigDecimal temperature;

    @Column(name = "PRESSURE", length = 10, nullable = false)
    @Comment("압력")
    private BigDecimal pressure;

    @Column(name = "WORKING_TIME", length = 10, nullable = false)
    @Comment("작동시간")
    private BigDecimal workingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVICE_STATUS", nullable = false)
    @Comment("기기상태")
    private DeviceStatus deviceStatus;

    @Column(name = "TIME_STAMP")
    @Comment("데이터 수집 시간")
    private LocalDateTime timeStamp;
}
