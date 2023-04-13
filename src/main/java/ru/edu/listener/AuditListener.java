package ru.edu.listener;

import java.time.Instant;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import ru.edu.entity.AuditableEntity;

public class AuditListener {

	@PrePersist
	public void prePersist(AuditableEntity<?> entity) {
		entity.setCreatedAt(Instant.now());
	}

	@PreUpdate
	public void preUpdate(AuditableEntity<?> entity) {
		entity.setUpdatedAt(Instant.now());
	}

}
