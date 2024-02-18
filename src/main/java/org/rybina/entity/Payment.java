package org.rybina.entity;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"receiver"})
//@OptimisticLocking(type = OptimisticLockType.DIRTY)
//@DynamicUpdate
public class Payment extends AuditableEntity<Integer> {
//    @Version
//    private Long version;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @PrePersist
    public void prePersist() {
        setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(Instant.now());
    }
}
