package com.app.repositories;

import com.app.models.MoneyTransaction;
import com.app.models.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {

    @Query("""
            select u.email
            from MoneyTransaction t
            join t.mainBill mb
            join mb.user u
            where t.id = :transactionId""")
    Optional<String> getUserEmail(Long transactionId);

    @Query("""
            select t
            from MoneyTransaction t
            join t.mainBill b
            join b.user u
            where u.id = :id and
            t.transactionDate > :from and t.transactionDate < :to
            and ((:type is NULL) or (:type = t.type))""")
    Page<MoneyTransaction> findByFilterWithUser(Long id,
                                                LocalDateTime from,
                                                LocalDateTime to,
                                                OperationType type,
                                                Pageable pageable);

    @Query("""
            select t
            from MoneyTransaction t
            join t.mainBill b
            where b.id = :id and
            t.transactionDate > :from and t.transactionDate < :to
            and ((:type is NULL) or (:type = t.type))""")
    Page<MoneyTransaction> findByFilterWithMainBill(Long id,
                                                    LocalDateTime from,
                                                    LocalDateTime to,
                                                    OperationType type,
                                                    Pageable pageable);

}
