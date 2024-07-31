package com.app.util.test_data;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class ExistedValidationAccordingAuthDataUtil {

    public static final Long RIGHT_ID = 1L;
    public static final Long SOME_ONE_USER_ID = 2L;
    public static final Long SOME_ONE_MAIN_BILL_ID = 3L;
    public static final Long SOME_ONE_SAVING_BILL_ID = 4L;
    public static final Long SOME_ONE_TRANSACTION_ID = 12L;
    public static final Long NOT_EXISTED_ID = 10000L;

    private ExistedValidationAccordingAuthDataUtil() {

    }

    public static Principal createPrincipal() {
        return new UsernamePasswordAuthenticationToken("ivan.ivanov@example.com", null, null);
    }
}
