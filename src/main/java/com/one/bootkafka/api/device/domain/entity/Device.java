package com.one.bootkafka.api.device.domain.entity;

import com.one.bootkafka.global.enums.device.DeviceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_DEVICE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVICE_SN", updatable = false, nullable = false)
    @Comment("기기 순번")
    private Long deviceId;

    @Column(name = "TEMPERATURE", length = 20, nullable = false)
    @Comment("온도")
    private Float temperature;

    @Column(name = "PRESSURE", length = 10, nullable = false)
    @Comment("압력")
    private Float pressure;

    @Column(name = "WORKING_TIME", length = 10, nullable = false)
    @Comment("작동시간")
    private Float workingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVICE_STATUS", nullable = false)
    @Comment("기기상태")
    private DeviceStatus deviceStatus;
}
