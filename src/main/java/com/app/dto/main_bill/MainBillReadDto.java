package com.app.dto.main_bill;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MainBillReadDto {
    private Long id;
    private LocalDateTime openDate;
    private BigDecimal balance;
    private Boolean isFrozen;
}
