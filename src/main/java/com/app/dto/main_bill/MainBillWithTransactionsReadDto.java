package com.app.dto.main_bill;


import com.app.dto.money_transaction.MoneyTransactionReadDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MainBillWithTransactionsReadDto {
    private Long id;
    private LocalDateTime openDate;
    private BigDecimal balance;
    private Boolean isFrozen;
    private List<MoneyTransactionReadDto> transactions;
}
