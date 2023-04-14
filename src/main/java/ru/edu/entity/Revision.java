package ru.edu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.edu.listener.MyRevisionListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// MyRevisionListener - листнер, который слушает событие на запись в Revision
@RevisionEntity(MyRevisionListener.class) // своя реализация вместо revinfo
public class Revision {

  /// 2 обязательных поля: rev и revtstmp
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @RevisionNumber // rev
  private Long id;

  @RevisionTimestamp // revtstmp
  private Long timestamp;

  // свои поля
  private String username;
}
