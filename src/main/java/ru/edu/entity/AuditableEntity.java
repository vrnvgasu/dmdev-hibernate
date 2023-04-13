package ru.edu.entity;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {

  @Column(name = "created_at")
  // java.time.Instant мапится на timestamp
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "created_by")
  private String createdBy;

  @PrePersist
  public void prePersist() {
    this.setCreatedAt(Instant.now());
  }

  @PreUpdate
  public void preUpdate() {
    this.setUpdatedAt(Instant.now());
  }

}
