package com.app.controllers;

import com.app.dto.response.JwtResponse;
import com.app.dto.user.LoginDto;
import com.app.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.app.utils.ResponseConstants.SUCCESS_LOGOUT;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        JwtResponse response = authenticationService.login(loginDto, request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    ResponseEntity<JwtResponse> refresh(@RequestBody String refresh, HttpServletRequest request) {
        JwtResponse response = authenticationService.refresh(refresh, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request, Principal principal) {
        authenticationService.logout(request, principal.getName());
        return ResponseEntity.ok(SUCCESS_LOGOUT);
    }


}
