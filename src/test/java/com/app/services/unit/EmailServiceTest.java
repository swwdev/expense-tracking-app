package com.app.services.unit;

import com.app.models.EmailToken;
import com.app.models.User;
import com.app.repositories.EmailTokenRepository;
import com.app.repositories.UserRepository;
import com.app.services.EmailService;
import com.app.services.UserService;
import com.app.util.test_data.DummyUserDataUtil;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.thymeleaf.TemplateEngine;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.app.util.test_data.DummyUserDataUtil.EMAIL;
import static com.app.util.test_data.DummyUserDataUtil.createUser;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    public static final String EMAIL_TOKEN = "email_token";
    private EmailToken emailToken;
    private User user;
    @Mock
    private EmailTokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        emailToken = EmailToken.builder()
                .token(EMAIL_TOKEN)
                .email(EMAIL)
                .expireDate(LocalDateTime.MAX)
                .build();
        user = createUser();

        Field confirmLifetime = emailService.getClass().getDeclaredField("confirmLifetime");
        confirmLifetime.setAccessible(true);
        confirmLifetime.set(emailService, Duration.ofDays(1));

        Field recoverLifetime = emailService.getClass().getDeclaredField("recoverLifetime");
        recoverLifetime.setAccessible(true);
        recoverLifetime.set(emailService, Duration.ofDays(1));

        Field from = emailService.getClass().getDeclaredField("from");
        from.setAccessible(true);
        from.set(emailService, "from");
    }

    @Test
    void sendRegistrationConfirmation() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doReturn(mimeMessage).when(mailSender).createMimeMessage(); //

        doReturn(new EmailToken()).when(tokenRepository).save(any());//
        doReturn("message").when(templateEngine).process(anyString(), any());//
        assertThatNoException().isThrownBy(() -> emailService.sendRegistrationConfirmation(DummyUserDataUtil.NAME, EMAIL));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void confirmNonexistentToken() {
        doReturn(Optional.empty()).when(tokenRepository).findByToken(EMAIL_TOKEN);

        assertThatThrownBy(() -> emailService.checkEmailToken(EMAIL_TOKEN)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void confirmExpiredToken() {
        emailToken.setExpireDate(LocalDateTime.MIN);
        doReturn(Optional.of(emailToken)).when(tokenRepository).findByToken(EMAIL_TOKEN);

        assertThatThrownBy(() -> emailService.checkEmailToken(EMAIL_TOKEN)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void successConfirmation() {
        doReturn(Optional.of(emailToken)).when(tokenRepository).findByToken(EMAIL_TOKEN);

        assertThatNoException().isThrownBy(() -> emailService.checkEmailToken(EMAIL_TOKEN));
    }

}
