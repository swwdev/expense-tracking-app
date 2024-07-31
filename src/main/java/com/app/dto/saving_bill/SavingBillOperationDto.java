package com.app.dto.saving_bill;


import com.app.utils.ResponseConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingBillOperationDto {
    @NotNull(message = ResponseConstants.BILL_ID_MUST_NOT_NULL)
    private Long mainBillId;

    @Positive(message = ResponseConstants.VALID_AMOUNT)
    @NotNull(message = ResponseConstants.NEED_AMOUNT)
    private BigDecimal amount;
}
