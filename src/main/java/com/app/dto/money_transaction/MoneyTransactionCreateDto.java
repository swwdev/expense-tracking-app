package com.app.dto.money_transaction;

import com.app.models.OperationType;
import com.app.utils.ResponseConstants;
import com.app.validation.annotations.ValidMainBillId;
import com.app.validation.groups.FirstGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, MoneyTransactionCreateDto.class})
public class MoneyTransactionCreateDto {

    @NotNull(message = ResponseConstants.NEED_OPERATION_TYPE)
    private OperationType type;

    @Positive(message = ResponseConstants.VALID_AMOUNT)
    @NotNull(message = ResponseConstants.NEED_AMOUNT)
    private BigDecimal amount;

    @NotBlank(message = ResponseConstants.REQUIRE_DESCRIPTION)
    private String description;

    @ValidMainBillId
    @NotNull(groups = FirstGroup.class, message = ResponseConstants.BILL_ID_MUST_NOT_NULL)
    private Long billId;
}
