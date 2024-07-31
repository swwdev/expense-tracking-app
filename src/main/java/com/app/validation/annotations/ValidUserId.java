package com.app.validation.annotations;

import com.app.utils.ResponseConstants;
import com.app.validation.validators.ValidUserIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidUserIdValidator.class)
public @interface ValidUserId {

    String message() default ResponseConstants.INVALID_USER_ID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
