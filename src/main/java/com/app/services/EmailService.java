package com.app.services;

import com.app.models.EmailToken;
import com.app.repositories.EmailTokenRepository;
import com.app.utils.ResponseConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailService {
    public static final String PASSWORD_CHANGE = "password change";
    public static final String RECOVER_PASSWORD = "recover password";
    private static final String ACCOUNT_VERIFICATION_SUBJECT = "Account verification";
    private final EmailTokenRepository emailTokenRepository;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${host-name}")
    private String hostName;

    @Value("${email.tokens.confirmation-lifetime}")
    private Duration confirmLifetime;

    @Value("${email.tokens.recover-lifetime}")
    private Duration recoverLifetime;

    @Async
    public void sendRegistrationConfirmation(String name, String email) {
        String token = saveToken(confirmLifetime, email);
        try {
            sendRegistrationMessage(email, token, name);
        } catch (MessagingException e) {
            log.error("Failed to send registration confirmation to email: {}", email, e);
        }
    }

    @Async
    public void sendChangePasswordNotification(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(PASSWORD_CHANGE);
        message.setFrom(from);
        message.setTo(to);
        message.setText(getChangePasswordText());
        mailSender.send(message);
    }

    @Async
    public void sendRecoverPasswordMessage(String email, String name) {
        String token = saveToken(recoverLifetime, email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(RECOVER_PASSWORD);
        message.setFrom(from);
        message.setTo(email);
        message.setText(getRecoverPasswordText(name, token));
        mailSender.send(message);

    }

    public EmailToken checkEmailToken(String token) {
        return emailTokenRepository.findByToken(token)
                .filter(emailToken -> LocalDateTime.now().isBefore(emailToken.getExpireDate()))
                .orElseThrow(() -> new AccessDeniedException(ResponseConstants.INVALID_TOKEN));
    }

    private void sendRegistrationMessage(String to, String token, String name) throws MessagingException {
        String mailText = getHtmlRegistrationMessage(token, name);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8.name());
        helper.setPriority(1);
        helper.setSubject(ACCOUNT_VERIFICATION_SUBJECT);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(mailText, true);
        mailSender.send(message);
    }

    private String saveToken(Duration recoverLifetime, String email) {
        String token = UUID.randomUUID().toString();
        EmailToken tokenEntity = EmailToken.builder()
                .token(token)
                .expireDate(LocalDateTime.now().plus(recoverLifetime))
                .email(email)
                .build();
        emailTokenRepository.save(tokenEntity);
        return token;
    }

    private String getHtmlRegistrationMessage(String token, String name) {
        String verificationUrl = hostName + "/users/activate-account?token=" + token;
        Context context = new Context();
        context.setVariables(Map.of("name", name, "url", verificationUrl));
        return templateEngine.process("email-template", context);
    }

    private String getRecoverPasswordText(String name, String token) {
        return "Hello " + name + "\nTo recover your password, please click on link below. If you did not request a password recovery, simply ignore this message."
                + "\n\n" + hostName + "/users/recover-password/confirm?token=" + token;
    }

    private String getChangePasswordText() {
        return "Hello\n\nYou have successfully changed your password. If you did not do this, please recover your password immediately.";
    }
}
