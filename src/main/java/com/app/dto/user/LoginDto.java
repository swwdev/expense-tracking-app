package com.app.dto.user;

import com.app.utils.ResponseConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @Email
    private String email;
    @NotBlank(message = ResponseConstants.NEED_NOT_BLANK_PASSWORD)
    private String password;
}
