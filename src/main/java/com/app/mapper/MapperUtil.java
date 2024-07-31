package com.app.mapper;

import com.app.exceptions.UserNotFoundException;
import com.app.models.MainBill;
import com.app.models.User;
import com.app.repositories.MainBillRepository;
import com.app.repositories.UserRepository;
import com.app.utils.ResponseConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Named("MapperUtil")
public class MapperUtil {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MainBillRepository mainBillRepository;

    @Named("now")
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Named("encode")
    public String encode(String source) {
        return passwordEncoder.encode(source);
    }

    @Named("getUser")
    public User getUser(Long id) {
        return userRepository.findById(id)
                .filter(User::getIsActive)
                .orElseThrow(UserNotFoundException::new);
    }

    @Named("getMainBill")
    public MainBill getMainBill(Long id) {
        return mainBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.MAIN_BILL_NOT_FOUND));
    }
}