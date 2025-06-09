package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CompressorId implements Serializable {

    private static final long serialVersionUID = 6545016837812039949L;

    @Size(max = 30)
    @NotNull
    @Column(name = "COMP_ID", nullable = false, length = 30)
    private String compId;

    @Size(max = 50)
    @NotNull
    @Column(name = "SCROLL_ID", nullable = false, length = 50)
    private String scrollId;



    @Override
    public int hashCode() {
        return Objects.hash(compId, scrollId);
    }

}