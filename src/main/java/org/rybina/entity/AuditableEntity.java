package org.rybina.entity;

import lombok.Getter;
import lombok.Setter;
import org.rybina.convertor.listener.AuditListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class AuditableEntity<T extends Serializable> extends BaseEntity<T> {

    private Instant createdAt;

    private Instant updatedAt;
}
