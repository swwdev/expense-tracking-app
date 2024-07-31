package com.app.util.test_data;

import com.app.dto.user.LoginDto;
import com.app.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ExistedUserDataUtil {

    public static final Long USER_ID = 1L;
    public static final String NAME = "Ivan";
    public static final String SURNAME = "Ivanov";
    public static final String EMAIL = "ivan.ivanov@example.com";
    public static final String RAW_PASS = "pass123";
    public static final String ENCRYPTED_PASS = "pass123";
    public static final LocalDateTime NOW_DATE = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
    public static final String EXPIRED_EMAIL_TOKEN = "token1";
    public static final String NOT_EXISTED_EMAIL_TOKEN = "dummy";
    public static final String VALID_EMAIL_TOKEN = "token3";
    private static final String PATRONYMIC = "Ivanovich";

    private ExistedUserDataUtil() {

    }

    public static User createUser() {
        return User.builder()
                .id(USER_ID)
                .name(NAME)
                .surname(SURNAME)
                .patronymic(PATRONYMIC)
                .email(EMAIL)
                .password(ENCRYPTED_PASS)
                .registrationDate(NOW_DATE)
                .isActive(true)
                .mainBills(new ArrayList<>())
                .savingBills(new ArrayList<>())
                .build();
    }


    public static LoginDto createLoginDto() {
        return LoginDto.builder()
                .email(EMAIL)
                .password(RAW_PASS)
                .build();
    }
}

