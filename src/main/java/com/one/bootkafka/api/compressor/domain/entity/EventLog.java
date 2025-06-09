package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "EVENT_LOG")
public class EventLog {

    @Id
    @Column(name = "EVENT_LOG_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로그 순번")
    private Long id;

    @Column(name = "SCROLL_ID", nullable = false, length = 50)
    @Comment("스크롤 아이디")
    private String scrollId;

    @Column(name = "COMP_ID", nullable = false, length = 30)
    @Comment("컴프레셔 아이디")
    private String compId;

    @Column(name = "DEVICE_TYPE", nullable = false, length = 20)
    @Comment("기기 타입 ('SCROLL' : 스크롤, 'COMPRESSOR' : 컴프레셔)")
    private String deviceType;

    @Column(name = "LOG_TYPE", nullable = false, length = 2)
    @Comment("로그 유형 ('01': sensor, '02': pressure_hi, '03': alarm, '04': temp_hi, '05':emergency, '06': suction_filter, '07': belt_tension, '08': tip_seal, '09': check_valve, '10': dust_seal)")
    private String logType;

    @CreatedDate
    @Column(name = "START_DT", nullable = false)
    @Comment("시작 일시")
    private LocalDate startDt;

    @Column(name = "END_DT")
    @Comment("종료 일시")
    private LocalDate endDt;
}