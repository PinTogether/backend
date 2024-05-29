package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "entity_status='ACTIVE'")
@SQLDelete(sql = "UPDATE pin_tag SET collection_status='DELETE' WHERE id=?")
public class PinTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pin pin;

    @NotNull
    @Column(length = 20)
    private String tag;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EntityStatus entityStatus;

    @Builder
    public PinTag(Pin pin, String tag, EntityStatus entityStatus) {
        this.pin = pin;
        this.tag = tag;
        this.entityStatus = entityStatus;
        this.pin.getPinTags().add(this);
    }
}
