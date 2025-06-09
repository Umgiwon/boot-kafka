package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "COMPRESSOR")
public class Compressor {

    @EmbeddedId
    private CompressorId id;

    @MapsId("scrollId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCROLL_ID", nullable = false)
    private Scroll scroll;

    @Builder.Default
    @NotNull
//    @ColumnDefault("0")
    @Column(name = "IS_COMP_RUNNING", nullable = false)
    private Boolean isCompRunning = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "TEMPERATURE", nullable = false)
    private Double temperature = 0.0;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "RUN_HOUR", nullable = false)
    private Integer runHour;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "IS_TRIP", nullable = false)
    private Boolean isTrip = false;

    @Size(max = 255)
    @Column(name = "ERROR_REASON")
    private String errorReason;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

}