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
@Table(name = "COMPRESSOR")
public class Compressor extends BaseTimeEntity {

    @EmbeddedId
    private CompressorId id;

    @MapsId("scrollId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCROLL_ID", referencedColumnName = "SCROLL_ID", nullable = false, updatable = false)
    @Comment("스크롤 아이디")
    private Scroll scroll;

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