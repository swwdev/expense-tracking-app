package com.app.dto.user;

import com.app.utils.ResponseConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserUpdatePersonalInfoDto {

    @NotNull(message = ResponseConstants.USER_ID_MUST_NOT_NULL)
    private Long id;

    @NotBlank(message = ResponseConstants.BLANK_NAME)
    private String name;

    @NotBlank(message = ResponseConstants.BLANK_SURNAME)
    private String surname;

    private String patronymic;

}
