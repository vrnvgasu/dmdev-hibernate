package ru.edu.dto;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ru.edu.entity.PersonalInfo;
import ru.edu.entity.Role;
import ru.edu.validation.UpdateCheck;

public record UserCreateDto(@Valid PersonalInfo personalInfo,
                            @NotNull String username,
                            UUID info,
                            // groups - просто метка. Указываем валидатору, когда проверять
                            @NotNull(groups = {UpdateCheck.class})
                            Role role,
                            Integer companyId) {

}
