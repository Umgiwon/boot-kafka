package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "EVENT_LOG")
public class EventLog {
    @Id
    @Column(name = "EVENT_LOG_SN", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "SCROLL_SN", nullable = false)
    private Long scrollSn;

    @Column(name = "COMP_ID")
    private Long compId;

    @Size(max = 20)
    @NotNull
    @Column(name = "DEVICE_TYPE", nullable = false, length = 20)
    private String deviceType;

    @Size(max = 2)
    @NotNull
    @Column(name = "LOG_TYPE", nullable = false, length = 2)
    private String logType;

    @NotNull
    @Column(name = "START_DT", nullable = false)
    private LocalDate startDt;

    @Column(name = "END_DT")
    private LocalDate endDt;

}