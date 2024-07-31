package com.app.repositories;

import com.app.models.MainBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MainBillRepository extends JpaRepository<MainBill, Long> {

    @Query("""
            select u.email
            from MainBill b
            join b.user u
            where b.id = :mainBillId""")
    Optional<String> getUserEmail(Long mainBillId);

    @Query("""
            select b
            from MainBill b
            left join fetch b.transactions
            where b.id = :id""")
    Optional<MainBill> findWithTransactionsById(Long id);

    @Query("""
            select b
            from MainBill b
            join fetch b.user
            where b.id = :id""")
    Optional<MainBill> findWithUserById(Long id);

}
