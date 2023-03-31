package ru.edu.entity;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {

  @Column(name = "created_at")
  // java.time.Instant мапится на timestamp
  private Instant createdAt;

  @Column(name = "created_by")
  private String createdBy;

}
