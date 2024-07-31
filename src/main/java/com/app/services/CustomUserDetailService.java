package com.app.services;

import com.app.dto.user.CustomUserDetails;
import com.app.models.User;
import com.app.repositories.UserRepository;
import com.app.utils.ResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .filter(User::getIsActive)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseConstants.EMAIL_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}
