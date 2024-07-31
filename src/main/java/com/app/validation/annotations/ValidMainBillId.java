package com.app.validation.annotations;

import com.app.utils.ResponseConstants;
import com.app.validation.validators.ValidMainBillIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {ValidMainBillIdValidator.class})
public @interface ValidMainBillId {

    String message() default ResponseConstants.INVALID_MAIN_BILL_ID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
