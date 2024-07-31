package com.app.services;

import com.app.repositories.EmailTokenRepository;
import com.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanExpireDataService {

    private final EmailTokenRepository emailTokenRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpireEmailTokens() {
        emailTokenRepository.deleteAllInBatch(emailTokenRepository.findExpiredToken());
        log.info("deleting expire email tokens");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanInactiveExpiredUsers() {
        userRepository.deleteAllInBatch(userRepository.findInactiveExpiredUsers());
        log.info("deleting inactive users whose verification tokens have expired");
    }

}
