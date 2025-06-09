package com.one.bootkafka.api.compressor.domain.entity;

import com.one.bootkafka.global.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "COMPRESSOR_HISTORY")
public class CompressorHistory extends BaseTimeEntity {

    @Id
    @Column(name = "HISTORY_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("이력 순번")
    private Long id;

    @Column(name = "COMP_ID", nullable = false, length = 30)
    @Comment("컴프레셔 아이디")
    private String compId;

    @Column(name = "SCROLL_ID", nullable = false, length = 50)
    @Comment("스크롤 아이디")
    private String scrollId;

    @Builder.Default
    @Column(name = "IS_COMP_RUNNING", nullable = false)
    @Comment("컴프레셔 작동 여부")
    private Boolean isCompRunning = false;

    @Builder.Default
    @Column(name = "TEMPERATURE", nullable = false)
    @Comment("온도")
    private Double temperature = 0.0;

    @Builder.Default
    @Column(name = "RUN_HOUR", nullable = false)
    @Comment("개별 누적 가동 시간")
    private Integer runHour = 0;

    @Builder.Default
    @Column(name = "IS_TRIP", nullable = false)
    @Comment("트립 여부")
    private Boolean isTrip = false;

    @Column(name = "ERROR_REASON")
    @Comment("에러 원인")
    private String errorReason;
}