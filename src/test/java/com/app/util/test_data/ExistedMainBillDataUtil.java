package com.app.util.test_data;

import com.app.dto.main_bill.MainBillCreateDto;

import java.math.BigDecimal;

public class ExistedMainBillDataUtil {
    public static final long EXISTED_ID = 1L;
    public static final long NOT_EXISTED_ID = 100L;

    private ExistedMainBillDataUtil() {

    }

    public static MainBillCreateDto createMainBillCreateDto() {
        return MainBillCreateDto.builder()
                .userId(1L)
                .initialBalance(BigDecimal.TEN)
                .build();
    }

}
