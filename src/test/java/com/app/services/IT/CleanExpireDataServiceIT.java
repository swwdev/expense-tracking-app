package com.app.services.IT;

import com.app.repositories.EmailTokenRepository;
import com.app.repositories.UserRepository;
import com.app.services.CleanExpireDataService;
import com.app.util.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CleanExpireDataServiceIT extends IntegrationTestBase {

    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CleanExpireDataService cleanExpireDataService;

    @Test
    void cleanExpireTokens() {
        cleanExpireDataService.cleanExpireEmailTokens();
        Assertions.assertThat(emailTokenRepository.findAll()).hasSize(2);
    }

    @Test
    void cleanInactiveExpiredUsers() {
        cleanExpireDataService.cleanInactiveExpiredUsers();
        Assertions.assertThat(userRepository.findAll()).hasSize(3);
    }
}
