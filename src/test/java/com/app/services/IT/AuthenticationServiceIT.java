package com.app.services.IT;

import com.app.dto.response.JwtResponse;
import com.app.dto.user.LoginDto;
import com.app.models.RefreshToken;
import com.app.repositories.RefreshTokenRepository;
import com.app.services.AuthenticationService;
import com.app.services.JwTokenService;
import com.app.util.IntegrationTestBase;
import com.app.util.test_data.TokenDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.app.util.test_data.ExistedUserDataUtil.EMAIL;
import static com.app.util.test_data.ExistedUserDataUtil.createLoginDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.USER_AGENT;

@Transactional
public class AuthenticationServiceIT extends IntegrationTestBase {

    private final LoginDto loginDto = createLoginDto();
    private MockHttpServletRequest request;
    @Autowired
    private RefreshTokenRepository tokenRepository;
    @Autowired
    private JwTokenService jwTokenService;
    @Autowired
    private AuthenticationService authenticationService;
    private RefreshToken savedRefresh;

    @BeforeEach
    void setTokens() {
        RefreshToken tokenEntity1 = RefreshToken.builder()
                .token(jwTokenService.generateRefreshJws(TokenDataUtil.FINGER_PRINT1, EMAIL))
                .fingerPrint(TokenDataUtil.FINGER_PRINT1)
                .email(EMAIL)
                .build();
        savedRefresh = tokenRepository.save(tokenEntity1);
    }


    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setRemoteAddr("0::1");
        request.addHeader(USER_AGENT, "browser1");
    }

    @Test
    void login() {
        JwtResponse jwtResponse = authenticationService.login(loginDto, request);
        assertAll(
                () -> {
                    Optional<RefreshToken> maybeToken = tokenRepository.findByToken(jwtResponse.getRefreshToken());
                    assertThat(maybeToken).isPresent();
                    assertThat(maybeToken.get().getId()).isNotEqualTo(savedRefresh.getId());
                },
                () -> assertThat(jwtResponse.getAccessToken()).isNotBlank())
        ;
    }

    @Test
    void refresh() {
        JwtResponse jwtResponse = authenticationService.refresh(savedRefresh.getToken(), request);
        assertAll(
                () -> assertThat(jwtResponse.getRefreshToken()).isNotBlank(),
                () -> assertThat(jwtResponse.getAccessToken()).isNotBlank())
        ;
    }

    @Test
    void logout() {
        authenticationService.logout(request, EMAIL);
        Assertions.assertThat(tokenRepository.findByFingerPrintAndEmail(TokenDataUtil.FINGER_PRINT1, EMAIL)).isNotPresent();
    }

}
