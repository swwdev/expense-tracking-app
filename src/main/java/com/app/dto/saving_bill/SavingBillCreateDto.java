package com.app.dto.saving_bill;

import com.app.utils.ResponseConstants;
import com.app.validation.annotations.ValidUserId;
import com.app.validation.groups.FirstGroup;
import jakarta.validation.GroupSequence;
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
@GroupSequence({FirstGroup.class, SavingBillCreateDto.class})
public class SavingBillCreateDto {

    @ValidUserId
    @NotNull(groups = FirstGroup.class, message = ResponseConstants.USER_ID_MUST_NOT_NULL)
    private Long userId;

    @Positive(message = ResponseConstants.VALID_AMOUNT)
    @NotNull(message = ResponseConstants.NEED_TARGET_AMOUNT)
    private BigDecimal targetAmount;

    private String description;
}
