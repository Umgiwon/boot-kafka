package com.one.bootkafka.api.compressor.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CompressorId implements Serializable {

    @Serial
    private static final long serialVersionUID = 6545016837812039949L;

    @Column(name = "COMP_ID", nullable = false, length = 30)
    @Comment("컴프레셔 아이디")
    private String compId;

    @Column(name = "SCROLL_ID", nullable = false, length = 50)
    @Comment("스크롤 아이디")
    private String scrollId;

    @Override
    public int hashCode() {
        return Objects.hash(compId, scrollId);
    }
}