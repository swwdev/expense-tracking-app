package com.app.services.IT;

import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.exceptions.UserNotFoundException;
import com.app.repositories.UserRepository;
import com.app.services.UserService;
import com.app.util.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.app.util.test_data.DummyUserDataUtil.OTHER_ID;
import static com.app.util.test_data.DummyUserDataUtil.USER_ID;
import static com.app.util.test_data.DummyUserDataUtil.createUserCreateDto;
import static com.app.util.test_data.DummyUserDataUtil.createUserUpdatePersonalInfoDto;
import static com.app.util.test_data.ExistedUserDataUtil.EMAIL;
import static com.app.util.test_data.ExistedUserDataUtil.EXPIRED_EMAIL_TOKEN;
import static com.app.util.test_data.ExistedUserDataUtil.NOT_EXISTED_EMAIL_TOKEN;
import static com.app.util.test_data.ExistedUserDataUtil.RAW_PASS;
import static com.app.util.test_data.ExistedUserDataUtil.VALID_EMAIL_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class UserServiceIT extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final UserCreateDto userCreateDto = createUserCreateDto();

    private final UserUpdatePersonalInfoDto updatePersonalInfoDto = createUserUpdatePersonalInfoDto();

    @Test
    void getById() {
        assertAll(
                () -> assertThat(userService.getById(USER_ID).getEmail()).isEqualTo(EMAIL),
                () -> assertThatThrownBy(() -> userService.getById(OTHER_ID)).isInstanceOf(UserNotFoundException.class)
        );
    }

    @Test
    void create() {
        assertAll(
                () -> {
                    userService.create(userCreateDto);
                    Assertions.assertThat(userRepository.findByEmail(userCreateDto.getEmail())).isPresent();
                },
                () -> {
                    userCreateDto.setEmail(EMAIL);
                    assertThatThrownBy(() -> userService.create(userCreateDto)).isInstanceOf(DataIntegrityViolationException.class);
                }
        );
    }

    @Test
    void delete() {
        assertAll(
                () -> {
                    userService.delete(USER_ID);
                    Assertions.assertThat(userRepository.findById(USER_ID)).isNotPresent();
                },
                () -> assertThatThrownBy(() -> userService.delete(OTHER_ID)).isInstanceOf(UserNotFoundException.class)
        );
    }

    @Test
    void update() {
        assertAll(
                () -> assertThatNoException().isThrownBy(() -> userService.update(updatePersonalInfoDto)),
                () -> {
                    updatePersonalInfoDto.setId(2L);
                    assertThrows(UserNotFoundException.class, () -> userService.update(updatePersonalInfoDto));
                }
        );
    }

    @Test
    void changePassword() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> userService.changePassword(EMAIL, "new-pass", "wrong-pass")),
                () -> assertThatNoException().
                        isThrownBy(() -> userService.changePassword(EMAIL, "new-pass", RAW_PASS))
        );
    }

    @Test
    void confirmRegistrationToken() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> userService.confirmRegistrationToken(EXPIRED_EMAIL_TOKEN)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> userService.confirmRegistrationToken(NOT_EXISTED_EMAIL_TOKEN)),
                () -> assertThat(userService.confirmRegistrationToken(VALID_EMAIL_TOKEN).getEmail())
                        .isEqualTo("sidor.sidorov@example.com")
        );
    }

    @Test
    void recoverPassword() {
        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> userService.recoverPassword(EXPIRED_EMAIL_TOKEN, "new")),
                () -> assertThrows(AccessDeniedException.class,
                        () -> userService.recoverPassword(NOT_EXISTED_EMAIL_TOKEN, "new")),
                () -> assertThatNoException()
                        .isThrownBy(() -> userService.recoverPassword("token2", "new"))
        );
    }

    @Test
    @Rollback
    void sendRecoverPasswordMessage() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class,
                        () -> userService.sendRecoverPasswordMessage("dummy")),
                () -> assertThatNoException()
                        .isThrownBy(() -> userService.sendRecoverPasswordMessage(EMAIL))
        );
    }
}
