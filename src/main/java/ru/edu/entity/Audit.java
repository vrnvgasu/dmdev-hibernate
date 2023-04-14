package ru.edu.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entity_id")
	private Serializable entityId;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "entity_content")
	private String entityContent;

	@Enumerated(EnumType.STRING)
	private Operation operation;

	public enum Operation {
		SAVE, UPDATE, DELETE, INSERT
	}

}
