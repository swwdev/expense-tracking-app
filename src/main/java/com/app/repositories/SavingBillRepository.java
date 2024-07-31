package com.app.repositories;

import com.app.models.SavingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SavingBillRepository extends JpaRepository<SavingBill, Long> {

    @Query("""
            select u.email
            from SavingBill sb
            join sb.user u
            where sb.id = :savingBillId""")
    Optional<String> getUserEmail(Long savingBillId);

    @Query("""
            select sb
            from SavingBill sb
            join fetch sb.user
            where sb.id = :id""")
    Optional<SavingBill> findWithUserById(Long id);

}
