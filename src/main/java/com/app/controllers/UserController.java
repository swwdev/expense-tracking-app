package com.app.controllers;

import com.app.dto.user.PasswordChangeDto;
import com.app.dto.user.PasswordRecoverDto;
import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserReadDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.dto.user.UserWithMainBillsReadDto;
import com.app.services.UserService;
import com.app.services.ValidationUserAccordingAuth;
import com.app.utils.ResponseConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.PERMANENT_REDIRECT;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ValidationUserAccordingAuth validationUserAccordingAuth;

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> get(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(id, principal);
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/with-main-bills/{id}")
    public ResponseEntity<UserWithMainBillsReadDto> getWithMainBills(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(id, principal);
        return ResponseEntity.ok(userService.getWithMainBillsById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid UserCreateDto user) {
        userService.create(user);
        return ResponseConstants.GO_ACTIVATE_RESPONSE + user.getEmail();
    }

    @PatchMapping
    public ResponseEntity<UserReadDto> update(@RequestBody @Valid UserUpdatePersonalInfoDto user, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(user.getId(), principal);
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(id, principal);
        userService.delete(id);
    }

    @PatchMapping("/change-password")
    public void changePassword(Principal principal, @RequestBody @Valid PasswordChangeDto passwordChangeDto) {
        userService.changePassword(principal.getName(), passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }

    @PostMapping("/activate-account")
    ResponseEntity<UserReadDto> activateUser(@RequestParam String token) {
        return ResponseEntity.ok().body(userService.confirmRegistrationToken(token));
    }

    @PostMapping("/recover-password/request")
    ResponseEntity<String> sendMailToRecover(@RequestBody String email) {
        userService.sendRecoverPasswordMessage(email);
        return ResponseEntity.ok().body(ResponseConstants.RECOVER_LETTER);
    }

    @GetMapping("/recover-password/confirm")
    ResponseEntity<String> redirectToPost(@RequestParam String token) {
        return new ResponseEntity<>(ResponseConstants.REDIRECT_TO_POST_REQUEST, PERMANENT_REDIRECT);
    }

    @PostMapping("/recover-password/confirm")
    ResponseEntity<String> recoverPassword(@RequestBody @Valid PasswordRecoverDto recoverDto) {
        userService.recoverPassword(recoverDto.getToken(), recoverDto.getNewPassword());
        return ResponseEntity.ok().body(ResponseConstants.SUCCESSFULLY_CHANGED);
    }
}
