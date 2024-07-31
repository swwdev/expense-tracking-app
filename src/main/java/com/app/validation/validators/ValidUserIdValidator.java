package com.app.validation.validators;

import com.app.exceptions.InvalidDataException;
import com.app.models.User;
import com.app.repositories.UserRepository;
import com.app.utils.ResponseConstants;
import com.app.validation.annotations.ValidUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, Long> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null)
            throw new InvalidDataException(ResponseConstants.ID_MUST_NOT_BE_NULL);
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .isPresent();
    }
}
