package com.app.util.test_data;

import com.app.dto.saving_bill.SavingBillCreateDto;
import com.app.dto.saving_bill.SavingBillOperationDto;

import java.math.BigDecimal;

public class ExistedSavingBillDataUtil {
    public static final Long EXISTED_ID = 1L;
    public static final Long NOT_EXISTED_ID = 1000L;


    private ExistedSavingBillDataUtil() {

    }

    public static SavingBillCreateDto createSavingBillCreateDto() {
        return SavingBillCreateDto.builder()
                .userId(1L)
                .targetAmount(BigDecimal.TEN)
                .build();
    }

    public static SavingBillOperationDto createOperationDto() {
        return SavingBillOperationDto.builder()
                .mainBillId(1L)
                .amount(BigDecimal.TEN)
                .build();
    }
}
