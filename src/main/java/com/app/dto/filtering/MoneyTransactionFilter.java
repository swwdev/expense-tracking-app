package com.app.dto.filtering;

import com.app.models.OperationType;
import com.app.utils.ResponseConstants;
import com.app.validation.annotations.ValidMainBillId;
import com.app.validation.annotations.ValidUserId;
import com.app.validation.groups.MainBillValidation;
import com.app.validation.groups.UserValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MoneyTransactionFilter {

    @ValidUserId(groups = UserValidation.class)
    @ValidMainBillId(groups = MainBillValidation.class)
    private final Long id;

    @NotNull(message = ResponseConstants.NEED_FROM_DATE)
    private final LocalDateTime from;

    @NotNull(message = ResponseConstants.NEED_TO_DATE)
    private final LocalDateTime to;

    private final OperationType operationType;
}
