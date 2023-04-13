package ru.edu.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"receiver"})
@Builder
@Entity
// аннотация вместо параметра в LockModeType.OPTIMISTIC в запросе
// OptimisticLockType.ALL - при изменении в where указываем все поля сущности
// OptimisticLockType.DIRTY - при изменении в where только измененные поля из контекста
//@OptimisticLocking(type = OptimisticLockType.DIRTY)
//@DynamicUpdate // при OptimisticLockType.ALL или DIRTY
public class Payment extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  // также нужен при LockModeType.PESSIMISTIC_FORCE_INCREMENT
  @Version // версия записи при type = OptimisticLockType.VERSION
    // при создании записи будет 0, при изменении 1, потом 2 и тд
  private Long version;

  @Column(nullable = false)
  private Integer amount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  private User receiver;

}
