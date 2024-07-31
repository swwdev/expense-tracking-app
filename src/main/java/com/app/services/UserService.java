package com.app.services;

import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserReadDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.dto.user.UserWithMainBillsReadDto;
import com.app.exceptions.UserNotFoundException;
import com.app.mapper.UserMapper;
import com.app.models.EmailToken;
import com.app.models.User;
import com.app.repositories.UserRepository;
import com.app.utils.ResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserReadDto getById(Long id) {
        return userMapper.toDto(findEntityById(id));
    }

    public UserWithMainBillsReadDto getWithMainBillsById(Long id) {
        return userRepository.findWithMainBillsById(id)
                .filter(User::getIsActive)
                .map(userMapper::toDtoWithMainBills)
                .orElseThrow(UserNotFoundException::new);
    }

    public void create(UserCreateDto user) {
        User savedUser = userRepository.save(userMapper.toEntity(user));
        emailService.sendRegistrationConfirmation(savedUser.getName(), savedUser.getEmail());
    }

    public void delete(Long id) {
        userRepository.delete(findEntityById(id));
    }

    public UserReadDto update(UserUpdatePersonalInfoDto user) {
        User updatedUser = findEntityById(user.getId());
        User updatedUserEntity = userMapper.copyNotNullFields(user, updatedUser);
        return userMapper.toDto(updatedUserEntity);
    }

    public void changePassword(String email, String newPassword, String oldPassword) {
        User user = findEntityByEmail(email);
        boolean matches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!matches) {
            throw new AccessDeniedException(ResponseConstants.ILLEGAL_PASSWORD_MESSAGE);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        emailService.sendChangePasswordNotification(email);
    }


    public UserReadDto confirmRegistrationToken(String token) {
        EmailToken emailToken = emailService.checkEmailToken(token);
        User user = userRepository.findByEmail(emailToken.getEmail())
                .orElseThrow(() -> new DataSourceLookupFailureException(ResponseConstants.USER_CONFIRM_LOOKUP_FAILURE));
        user.setIsActive(true);
        return userMapper.toDto(user);
    }

    public void recoverPassword(String token, String newPassword) {
        String email = emailService.checkEmailToken(token).getEmail();
        findEntityByEmail(email).setPassword(passwordEncoder.encode(newPassword));
    }

    public void sendRecoverPasswordMessage(String email) { //need throw exception in main thread to catch it
        User user = findEntityByEmail(email);
        emailService.sendRecoverPasswordMessage(email, user.getName());
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .orElseThrow(UserNotFoundException::new);
    }

    private User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::getIsActive)
                .orElseThrow(UserNotFoundException::new);
    }


}
