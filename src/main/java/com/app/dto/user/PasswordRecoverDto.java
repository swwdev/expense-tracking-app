package com.app.dto.user;

import com.app.utils.Constants;
import com.app.utils.ResponseConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordRecoverDto {
    @NotBlank(message = ResponseConstants.NEED_EMAIL_TOKEN)
    private String token;

    @Pattern(regexp = Constants.PASSWORD_REGEXP, message = ResponseConstants.WEAK_PASSWORD)
    @NotBlank(message = ResponseConstants.NEED_NOT_BLANK_NEW_PASSWORD)
    private String newPassword;
}
