package com.app.util.test_data;

import com.app.dto.user.LoginDto;
import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserReadDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.models.User;

import java.time.LocalDateTime;


public class DummyUserDataUtil {

    public static final String NEW_PASSWORD = "new password";
    public static final Long USER_ID = 1L;
    public static final Long OTHER_ID = 2000L;
    public static final String NAME = "name";
    public static final String OTHER_NAME = "other name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "em.email@example.com";
    public static final String RAW_PASS = "pass123";
    public static final String ENCRYPTED_PASS = "{bcrypt}pass123";
    public static final LocalDateTime NOW_DATE = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
    private static final String PATRONYMIC = "patronymic";
    private DummyUserDataUtil() {

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
                .mainBills(null)
                .savingBills(null)
                .build();
    }

    public static UserReadDto createUserReadDto() {
        return UserReadDto.builder()
                .id(USER_ID)
                .name(NAME)
                .surname(SURNAME)
                .patronymic(PATRONYMIC)
                .email(EMAIL)
                .registrationDate(NOW_DATE)
                .build();
    }

    public static UserCreateDto createUserCreateDto() {
        return UserCreateDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .patronymic(PATRONYMIC)
                .email(EMAIL)
                .password(RAW_PASS)
                .build();
    }

    public static UserUpdatePersonalInfoDto createUserUpdatePersonalInfoDto() {
        return UserUpdatePersonalInfoDto.builder()
                .id(USER_ID)
                .name(NAME)
                .surname(SURNAME)
                .patronymic(PATRONYMIC)
                .build();
    }

    public static LoginDto createLoginDto() {
        return LoginDto.builder()
                .email(EMAIL)
                .password(RAW_PASS)
                .build();
    }
}
