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
@Table(name = "SCROLL_HISTORY")
public class ScrollHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_SN", nullable = false, updatable = false)
    @Comment("이력 순번")
    private Long id;

    @Column(name = "SCROLL_ID", nullable = false, updatable = false, length = 50)
    @Comment("스크롤 아이디")
    private String scrollId;

    @Builder.Default
    @Column(name = "IS_SCROLL_RUNNING", nullable = false)
    @Comment("스크롤 작동여부")
    private Boolean isScrollRunning = false;

    @Builder.Default
    @Column(name = "PRESSURE", nullable = false)
    @Comment("전체 압력")
    private Double pressure = 0.0;

    @Builder.Default
    @Column(name = "SET_PRESSURE", nullable = false)
    @Comment("설정 압력")
    private Double setPressure = 0.0;

    @Builder.Default
    @Column(name = "TOTAL_RUN_HOURS", nullable = false)
    @Comment("전체 누적 가동 시간")
    private Integer totalRunHours = 0;

    @Lob
    @Column(name = "RAW_DATA_JSON", nullable = false)
    @Comment("수신 데이터(json)")
    private String rawDataJson;
}