package com.app.dto.saving_bill;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SavingBillReadDto {
    private Long id;
    private LocalDateTime openDate;
    private BigDecimal balance;
    private BigDecimal target;
    private String description;
}
