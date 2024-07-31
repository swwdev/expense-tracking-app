package com.app.repositories;

import com.app.models.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);

    @Query("""
            select t
            from EmailToken t
            where t.expireDate <= CURRENT_TIMESTAMP
            """)
    List<EmailToken> findExpiredToken();

}
