package com.app.validation.validators;

import com.app.exceptions.InvalidDataException;
import com.app.repositories.MainBillRepository;
import com.app.utils.ResponseConstants;
import com.app.validation.annotations.ValidMainBillId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidMainBillIdValidator implements ConstraintValidator<ValidMainBillId, Long> {
    private final MainBillRepository mainBillRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null)
            throw new InvalidDataException(ResponseConstants.ID_MUST_NOT_BE_NULL);
        return mainBillRepository.findById(id).isPresent();
    }
}
