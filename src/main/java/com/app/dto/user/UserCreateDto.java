package com.app.dto.user;

import com.app.utils.Constants;
import com.app.utils.ResponseConstants;
import com.app.validation.annotations.Unique;
import com.app.validation.groups.FirstGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, UserCreateDto.class})
public class UserCreateDto {

    @NotBlank(message = ResponseConstants.BLANK_NAME)
    private String name;

    @NotBlank(message = ResponseConstants.BLANK_SURNAME)
    private String surname;

    private String patronymic;

    @Unique
    @Email
    @NotBlank(groups = FirstGroup.class, message = ResponseConstants.NEED_NOT_BLANK_EMAIL)
    private String email;

    @Pattern(regexp = Constants.PASSWORD_REGEXP, message = ResponseConstants.WEAK_PASSWORD)
    @NotBlank(message = ResponseConstants.NEED_NOT_BLANK_PASSWORD)
    private String password;

}
