package com.app.util.test_data;

import com.app.dto.filtering.MoneyTransactionFilter;
import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.models.OperationType;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExistedMoneyTransactionsDataUtil {


    public static final long UNFROZEN_EMPTY_BILL_ID = 1L;
    public static final long UNFROZEN_NOT_EMPTY_BILL_ID = 5L;
    public static final long FROZEN_BILL_ID = 2L;
    public static final long EXISTED_ID = 2L;
    public static final long NOT_EXISTED_ID = 200L;


    private ExistedMoneyTransactionsDataUtil() {

    }

    public static MoneyTransactionCreateDto createMoneyTransactionCreateDto() {
        return MoneyTransactionCreateDto.builder()
                .billId(UNFROZEN_NOT_EMPTY_BILL_ID)
                .description("test transaction")
                .type(OperationType.withdrawal)
                .amount(BigDecimal.TEN)
                .build();
    }

    public static Pageable getPageable() {
        return Pageable.ofSize(15);
    }

    public static MoneyTransactionFilter getFilter() {
        return MoneyTransactionFilter.builder()
                .id(1L)
                .from(LocalDateTime.of(2000, 1, 1, 1, 1))
                .to(LocalDateTime.of(3000, 1, 1, 1, 1))
                .build();
    }


}
