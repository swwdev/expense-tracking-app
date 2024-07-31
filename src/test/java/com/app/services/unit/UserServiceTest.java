package com.app.services.unit;

import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserReadDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.exceptions.UserNotFoundException;
import com.app.mapper.UserMapper;
import com.app.models.User;
import com.app.repositories.UserRepository;
import com.app.services.EmailService;
import com.app.services.UserService;
import com.app.util.test_data.DummyUserDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.app.util.test_data.DummyUserDataUtil.EMAIL;
import static com.app.util.test_data.DummyUserDataUtil.NAME;
import static com.app.util.test_data.DummyUserDataUtil.NEW_PASSWORD;
import static com.app.util.test_data.DummyUserDataUtil.OTHER_ID;
import static com.app.util.test_data.DummyUserDataUtil.OTHER_NAME;
import static com.app.util.test_data.DummyUserDataUtil.RAW_PASS;
import static com.app.util.test_data.DummyUserDataUtil.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock(strictness = LENIENT)
    private UserMapper userMapper;
    @Mock(strictness = LENIENT)
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;

    private UserReadDto userReadDto;

    private UserCreateDto userCreateDto;

    private UserUpdatePersonalInfoDto userUpdatePersonalInfoDto;

    @BeforeEach
    void setUp() {
        user = DummyUserDataUtil.createUser();
        userCreateDto = DummyUserDataUtil.createUserCreateDto();
        userUpdatePersonalInfoDto = DummyUserDataUtil.createUserUpdatePersonalInfoDto();
        userReadDto = DummyUserDataUtil.createUserReadDto();


        doReturn(Optional.empty()).when(userRepository).findById(DummyUserDataUtil.OTHER_ID);

        doReturn(userReadDto).when(userMapper).toDto(user);
    }

    @Test
    void getById() {
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);

        assertThat(userService.getById(USER_ID)).isEqualTo(userReadDto);
        user.setId(OTHER_ID);
        assertThatThrownBy(() -> userService.getById(OTHER_ID)).isInstanceOf(UserNotFoundException.class);
        user.setIsActive(false);
        assertThatThrownBy(() -> userService.getById(OTHER_ID)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void create() {
        User transientUser = DummyUserDataUtil.createUser();
        transientUser.setId(null);
        user.setIsActive(false);

        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doReturn(transientUser).when(userMapper).toEntity(userCreateDto);
        doReturn(user).when(userRepository).save(transientUser);
        doNothing().when(emailService).sendRegistrationConfirmation(NAME, EMAIL);

        assertThatNoException().isThrownBy(() -> userService.create(userCreateDto));
        verify(emailService).sendRegistrationConfirmation(NAME, EMAIL);
        verify(userRepository).save(transientUser);
    }

    @Test
    void delete() {
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doNothing().when(userRepository).delete(user);

        assertThatNoException().isThrownBy(() -> userService.delete(USER_ID));
        assertThatThrownBy(() -> userService.delete(OTHER_ID)).isInstanceOf(UserNotFoundException.class);
        user.setIsActive(false);
        assertThatThrownBy(() -> userService.delete(OTHER_ID)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void update() {
        User withChangedName = DummyUserDataUtil.createUser();
        withChangedName.setName(OTHER_NAME);
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doReturn(user).when(userMapper).copyNotNullFields(userUpdatePersonalInfoDto, withChangedName);
        doReturn(user).when(userRepository).save(user);

        user.setName(OTHER_NAME);

        assertThat(userService.update(userUpdatePersonalInfoDto)).isEqualTo(userReadDto);
        userUpdatePersonalInfoDto.setId(OTHER_ID);
        assertThatThrownBy(() -> userService.update(userUpdatePersonalInfoDto)).isInstanceOf(UserNotFoundException.class);
        user.setIsActive(false);
        assertThatThrownBy(() -> userService.update(userUpdatePersonalInfoDto)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void successChangePassword() {
        doReturn(Optional.of(user)).when(userRepository).findByEmail(EMAIL);
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        assertThatNoException().isThrownBy(() -> userService.changePassword(EMAIL, NEW_PASSWORD, RAW_PASS));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void FailedChangePassword() {
        doReturn(Optional.of(user)).when(userRepository).findByEmail(EMAIL);
        doReturn(false).when(passwordEncoder).matches(anyString(), anyString());

        assertThatThrownBy(() -> userService.changePassword(EMAIL, NEW_PASSWORD, RAW_PASS)).isInstanceOf(AccessDeniedException.class);
        verify(passwordEncoder, never()).encode(anyString());
    }
}