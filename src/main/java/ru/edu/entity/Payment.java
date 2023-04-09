package ru.edu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"receiver"})
@Builder
@Entity
// аннотация вместо параметра в LockModeType.OPTIMISTIC в запросе
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Version // версия записи при type = OptimisticLockType.VERSION
    // при создании записи будет 0, при изменении 1, потом 2 и тд
  private Long version;

  @Column(nullable = false)
  private Integer amount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  private User receiver;

}
