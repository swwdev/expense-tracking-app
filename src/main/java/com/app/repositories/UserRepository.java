package com.app.repositories;

import com.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
            select u.email
            from User u
            where u.id = :id""")
    Optional<String> getEmail(Long id);

    @Query("""
            select u
            from User u
            left join fetch u.mainBills
            where u.id = :id""")
    Optional<User> findWithMainBillsById(Long id);

    @Query(nativeQuery = true, value = """
            select *
            from users
            where registration_date < CURRENT_TIMESTAMP - interval '1' day
            and is_active = false""")
    List<User> findInactiveExpiredUsers();
}