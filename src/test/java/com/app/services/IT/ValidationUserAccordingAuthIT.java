package com.app.services.IT;

import com.app.services.ValidationUserAccordingAuth;
import com.app.util.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.NOT_EXISTED_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.RIGHT_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.SOME_ONE_MAIN_BILL_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.SOME_ONE_SAVING_BILL_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.SOME_ONE_TRANSACTION_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.SOME_ONE_USER_ID;
import static com.app.util.test_data.ExistedValidationAccordingAuthDataUtil.createPrincipal;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class ValidationUserAccordingAuthIT extends IntegrationTestBase {

    private final Principal principal = createPrincipal();
    @Autowired
    private ValidationUserAccordingAuth validationUserAccordingAuth;

    @Test
    void validateUserObtainThisMainBill() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> validationUserAccordingAuth.validateUserObtainThisMainBill(SOME_ONE_MAIN_BILL_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainThisMainBill(RIGHT_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainThisMainBill(NOT_EXISTED_ID, principal))
        );
    }

    @Test
    void validateUserObtainUser() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> validationUserAccordingAuth.validateUserObtainUser(SOME_ONE_USER_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainUser(RIGHT_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainUser(NOT_EXISTED_ID, principal))
        );
    }

    @Test
    void validateUserObtainSavingBill() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> validationUserAccordingAuth.validateUserObtainSavingBill(SOME_ONE_SAVING_BILL_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainSavingBill(RIGHT_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainSavingBill(NOT_EXISTED_ID, principal))
        );
    }

    @Test
    void validateUserObtainTransaction() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> validationUserAccordingAuth.validateUserObtainTransaction(SOME_ONE_TRANSACTION_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainTransaction(RIGHT_ID, principal)),
                () -> assertThatNoException()
                        .isThrownBy(() -> validationUserAccordingAuth.validateUserObtainTransaction(NOT_EXISTED_ID, principal))
        );
    }
}
