package com.app.validation.validators;

import com.app.exceptions.InvalidMainBillException;
import com.app.models.SavingBill;
import com.app.repositories.MainBillRepository;
import com.app.repositories.SavingBillRepository;
import com.app.utils.ResponseConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class BelongBillValidator {
    private final MainBillRepository mainBillRepository;
    private final SavingBillRepository savingBillRepository;

    public void validateBills(Long mainBillId, Long savingBillId) {
        SavingBill savingBill = savingBillRepository.findWithUserById(savingBillId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.SAVING_BILL_NOT_FOUND));

        mainBillRepository.findWithUserById(mainBillId)
                .filter(mainBill -> mainBill.getUser().equals(savingBill.getUser()))
                .orElseThrow(() -> new InvalidMainBillException(ResponseConstants.TRANSFER_FROM_SOMEONE_MAIN_BILL));
    }
}
