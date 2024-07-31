package com.app.services.unit;

import com.app.dto.response.JwtResponse;
import com.app.dto.user.CustomUserDetails;
import com.app.dto.user.LoginDto;
import com.app.exceptions.CantObtainFingerPrintException;
import com.app.models.RefreshToken;
import com.app.models.User;
import com.app.repositories.RefreshTokenRepository;
import com.app.services.AuthenticationService;
import com.app.services.JwTokenService;
import com.app.util.test_data.TokenDataUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

import static com.app.util.test_data.DummyUserDataUtil.EMAIL;
import static com.app.util.test_data.DummyUserDataUtil.RAW_PASS;
import static com.app.util.test_data.DummyUserDataUtil.createLoginDto;
import static com.app.util.test_data.DummyUserDataUtil.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.USER_AGENT;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private final User user = createUser();
    private final LoginDto loginDto = createLoginDto();
    @Mock
    JwTokenService tokenService;
    @Mock
    AuthenticationManager authManager;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock(strictness = LENIENT)
    EntityManager entityManager;
    @InjectMocks
    AuthenticationService authenticationService;
    private MockHttpServletRequest request1;
    private MockHttpServletRequest request2;

    @BeforeEach
    void setUp() {
        request1 = new MockHttpServletRequest();
        request1.setRemoteAddr("0::1");
        request1.addHeader(USER_AGENT, "browser1");

        request2 = new MockHttpServletRequest();
        request2.setRemoteAddr("0::2");
        request2.addHeader(USER_AGENT, "browser2");

        doNothing().when(entityManager).flush();
    }

    @Test
    void login() {
        doReturn(new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                new ArrayList<>()))
                .when(authManager).authenticate(new UsernamePasswordAuthenticationToken(EMAIL, RAW_PASS));

        Mockito.doReturn(TokenDataUtil.ACCESS).when(tokenService).generateAccessJws(EMAIL);
        Mockito.doReturn(TokenDataUtil.REFRESH).when(tokenService).generateRefreshJws(TokenDataUtil.FINGER_PRINT1, EMAIL);

        assertThat(authenticationService.login(loginDto, request1))
                .isEqualTo(new JwtResponse(TokenDataUtil.ACCESS, TokenDataUtil.REFRESH));

        verify(tokenService).revokeTokenByFingerPrintAndEmail(TokenDataUtil.FINGER_PRINT1, EMAIL);
    }


    @Test
    void refreshIfTokenAlreadyRevoked() {
        doReturn(Optional.empty()).when(refreshTokenRepository).findByToken(TokenDataUtil.REFRESH);
        Mockito.doReturn(TokenDataUtil.FINGER_PRINT1).when(tokenService).getFingerPrint(TokenDataUtil.REFRESH);
        doReturn(EMAIL).when(tokenService).getEmail(TokenDataUtil.REFRESH);

        Assertions.assertThrows(AccessDeniedException.class,
                () -> authenticationService.refresh(TokenDataUtil.REFRESH, request1));

        verify(tokenService).revokeTokenByFingerPrintAndEmail(anyString(), anyString());
    }

    @Test
    void refreshIfFingerPrintDiffer() {
        doReturn(Optional.of(new RefreshToken())).when(refreshTokenRepository).findByToken(TokenDataUtil.REFRESH);
        Mockito.doReturn(TokenDataUtil.FINGER_PRINT2).when(tokenService).getFingerPrint(TokenDataUtil.REFRESH);
        doReturn(EMAIL).when(tokenService).getEmail(TokenDataUtil.REFRESH);

        Assertions.assertThrows(AccessDeniedException.class,
                () -> authenticationService.refresh(TokenDataUtil.REFRESH, request1));

        verify(refreshTokenRepository).delete(any());
    }


    @Test
    void successRefresh() {

        doReturn(Optional.of(new RefreshToken())).when(refreshTokenRepository).findByToken(TokenDataUtil.REFRESH);
        Mockito.doReturn(TokenDataUtil.FINGER_PRINT1).when(tokenService).getFingerPrint(TokenDataUtil.REFRESH);
        doReturn(EMAIL).when(tokenService).getEmail(TokenDataUtil.REFRESH);
        Mockito.doReturn(TokenDataUtil.ACCESS).when(tokenService).generateAccessJws(EMAIL);
        Mockito.doReturn(TokenDataUtil.REFRESH).when(tokenService).generateRefreshJws(TokenDataUtil.FINGER_PRINT1, EMAIL);


        assertThat(authenticationService.refresh(TokenDataUtil.REFRESH, request1))
                .isEqualTo(new JwtResponse(TokenDataUtil.ACCESS, TokenDataUtil.REFRESH));

        verify(refreshTokenRepository).delete(any());
    }

    @Test
    void logout() {
        doNothing().when(tokenService).revokeTokenByFingerPrintAndEmail(TokenDataUtil.FINGER_PRINT1, EMAIL);

        assertThatNoException().isThrownBy(() -> authenticationService.logout(request1, EMAIL));

        verify(tokenService).revokeTokenByFingerPrintAndEmail(TokenDataUtil.FINGER_PRINT1, EMAIL);
        verify(entityManager).flush();
    }

    @Test
    void persistNewRefreshToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.doReturn(TokenDataUtil.REFRESH).when(tokenService).generateRefreshJws(TokenDataUtil.FINGER_PRINT1, EMAIL);
        doReturn(new RefreshToken()).when(refreshTokenRepository).save(any());

        Method persistNewRefreshToken = AuthenticationService.class.getDeclaredMethod("persistNewRefreshToken", String.class, String.class);
        persistNewRefreshToken.setAccessible(true);

        assertThat(persistNewRefreshToken.invoke(authenticationService, TokenDataUtil.FINGER_PRINT1, EMAIL))
                .isEqualTo(TokenDataUtil.REFRESH);

        Mockito.verify(refreshTokenRepository).save(any());
    }

    @Test
    void getFingerPrint() throws NoSuchMethodException {
        Method getFingerPrint = AuthenticationService.class.getDeclaredMethod("getFingerPrint", HttpServletRequest.class);
        getFingerPrint.setAccessible(true);

        Assertions.assertAll(
                () -> assertThat(getFingerPrint.invoke(authenticationService, request1))
                        .isEqualTo(TokenDataUtil.FINGER_PRINT1),
                () -> assertThat(getFingerPrint.invoke(authenticationService, request2))
                        .isEqualTo(TokenDataUtil.FINGER_PRINT2)
        );
    }

    @Test
    void getFingerPrintIfFingerPrintIsInvalid() throws NoSuchMethodException {
        Method getFingerPrint = AuthenticationService.class.getDeclaredMethod("getFingerPrint", HttpServletRequest.class);
        getFingerPrint.setAccessible(true);

        request1.setRemoteAddr(null);
        request2.removeHeader(USER_AGENT);
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> {
                    try {
                        getFingerPrint.invoke(authenticationService, request1);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }).isInstanceOf(CantObtainFingerPrintException.class),
                () -> assertThatThrownBy(() -> {
                    try {
                        getFingerPrint.invoke(authenticationService, request2);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }).isInstanceOf(CantObtainFingerPrintException.class)
        );
    }


}
