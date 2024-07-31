package com.app.services.IT;

import com.app.dto.user.CustomUserDetails;
import com.app.models.User;
import com.app.repositories.UserRepository;
import com.app.services.CustomUserDetailService;
import com.app.util.IntegrationTestBase;
import com.app.util.test_data.DummyUserDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static com.app.util.test_data.ExistedUserDataUtil.EMAIL;
import static com.app.util.test_data.ExistedUserDataUtil.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class CustomUserDetailServiceIT extends IntegrationTestBase {
    private final User userEntity = createUser();
    @Autowired
    CustomUserDetailService userDetailService;

    @Autowired
    UserRepository userRepository;

    @Test
    void loadUserByUserName() {
        assertAll(
                () -> assertThat((userDetailService.loadUserByUsername(EMAIL)))
                        .isEqualTo(new CustomUserDetails(userEntity)),
                () -> assertThrows(UsernameNotFoundException.class,
                        () -> userDetailService.loadUserByUsername(DummyUserDataUtil.EMAIL)
                )
        );
    }
}
