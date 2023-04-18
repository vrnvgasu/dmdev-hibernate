package ru.edu.dto;

import java.util.UUID;
import ru.edu.entity.PersonalInfo;
import ru.edu.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          UUID info,
                          Role role,
                          CompanyReadDto company) {

}
