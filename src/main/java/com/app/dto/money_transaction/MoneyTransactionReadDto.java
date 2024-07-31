package com.app.dto.money_transaction;

import com.app.models.OperationType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MoneyTransactionReadDto {
    private Long id;
    private OperationType type;
    private LocalDateTime completedIn;
    private BigDecimal amount;
    private String description;
}
