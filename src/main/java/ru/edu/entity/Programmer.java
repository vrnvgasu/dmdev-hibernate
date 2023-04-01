package ru.edu.entity;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//значение для колонки type
@DiscriminatorValue("programmer")
public class Programmer extends User {

  @Enumerated(EnumType.STRING)
  private Language language;

  @Builder
  public Programmer(Long id, PersonalInfo personalInfo, String username, Role role, String info, Company company,
    Profile profile, List<UserChat> userChats, Language language) {
    super(id, personalInfo, username, role, info, company, profile, userChats);
    this.language = language;
  }

}
