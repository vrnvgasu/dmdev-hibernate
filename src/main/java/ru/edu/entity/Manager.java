package ru.edu.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// наш первичный ключ является внешним ключом на User
// при стратегии InheritanceType.JOINED
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {

  @Column(name = "project_name")
  private String projectName;

//  @Builder
  public Manager(Long id, PersonalInfo personalInfo, String username, Role role, UUID info, Company company,
    Profile profile, Set<UserChat> userChats, String projectName) {
    super(id, personalInfo, username, role, info, company, /*profile, */userChats, new ArrayList<>());
    this.projectName = projectName;
  }

}
