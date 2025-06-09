package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "COMPRESSOR_HISTORY")
public class CompressorHistory {
    @Id
    @Column(name = "HISTORY_SN", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "COMP_ID", nullable = false, length = 30)
    private String compId;

    @Size(max = 50)
    @NotNull
    @Column(name = "SCROLL_ID", nullable = false, length = 50)
    private String scrollId;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "IS_COMP_RUNNING", nullable = false)
    private Boolean isCompRunning = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "TEMPERATURE", nullable = false)
    private Double temperature;

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