package com.app.services;

import com.app.repositories.MainBillRepository;
import com.app.repositories.MoneyTransactionRepository;
import com.app.repositories.SavingBillRepository;
import com.app.repositories.UserRepository;
import com.app.utils.ResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidationUserAccordingAuth {
    private final MainBillRepository mainBillRepository;
    private final UserRepository userRepository;
    private final SavingBillRepository savingBillRepository;
    private final MoneyTransactionRepository transactionRepository;

    public void validateUserObtainThisMainBill(Long mainBillId, Principal principal) {
        Boolean granted = mainBillRepository.getUserEmail(mainBillId)
                .map(email -> email.equals(principal.getName()))
                .orElse(true); //will throw entity not found exception after
        if (!granted)
            throw new AccessDeniedException(ResponseConstants.NO_ACCESS);
    }

    public void validateUserObtainUser(Long userId, Principal principal) {
        Boolean granted = userRepository.getEmail(userId)
                .map(email -> email.equals(principal.getName()))
                .orElse(true); //will throw entity not found exception after
        if (!granted)
            throw new AccessDeniedException(ResponseConstants.NO_ACCESS);
    }

    public void validateUserObtainSavingBill(Long savingBillId, Principal principal) {
        Boolean granted = savingBillRepository.getUserEmail(savingBillId)
                .map(email -> email.equals(principal.getName()))
                .orElse(true); //will throw entity not found exception after
        if (!granted)
            throw new AccessDeniedException(ResponseConstants.NO_ACCESS);

    }

    public void validateUserObtainTransaction(Long transactionId, Principal principal) {
        Boolean granted = transactionRepository.getUserEmail(transactionId)
                .map(email -> email.equals(principal.getName()))
                .orElse(true);
        if (!granted)
            throw new AccessDeniedException(ResponseConstants.NO_ACCESS);
    }
}

