package ru.edu.listener;

import org.hibernate.event.spi.AbstractPreDatabaseOperationEvent;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import ru.edu.entity.Audit;
import ru.edu.entity.Audit.Operation;

/**
 * Своя реализация Audit для отслеживания операций с сущностями
 */

// PreDeleteEventListener, PreInsertEventListener из группы интерфейсов листнеров
public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

  @Override
  public boolean onPreDelete(PreDeleteEvent event) {
    auditEntity(event, Operation.DELETE);

    // false - не нужно запрещать операцию, т.е. все ок
    return false;
  }

  @Override
  public boolean onPreInsert(PreInsertEvent event) {
    auditEntity(event, Operation.INSERT);
    return false;
  }

  private void auditEntity(AbstractPreDatabaseOperationEvent event, Operation operation) {
    // Audit тоже порождает event при сохранении
    if (event.getEntity().getClass() != Audit.class) {
      var audit = Audit.builder()
        .entityId(event.getId())
        .entityName(event.getEntityName())
        .entityContent(event.getEntity().toString())
        .operation(operation)
        .build();

      // в event есть сессия.
      // С ее помощью может сделать сохранение сущности Audit
      event.getSession().save(audit);
    }
  }

}
