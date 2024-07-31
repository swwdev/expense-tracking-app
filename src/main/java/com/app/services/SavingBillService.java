package com.app.services;

import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.dto.saving_bill.SavingBillCreateDto;
import com.app.dto.saving_bill.SavingBillOperationDto;
import com.app.dto.saving_bill.SavingBillReadDto;
import com.app.exceptions.NotEnoughBalance;
import com.app.mapper.SavingBillMapper;
import com.app.models.OperationType;
import com.app.models.SavingBill;
import com.app.repositories.SavingBillRepository;
import com.app.utils.ResponseConstants;
import com.app.validation.validators.BelongBillValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class SavingBillService {
    public static final String TRANSFER_TO_SAVING_BILL = "transfer to saving bill";
    public static final String TRANSFER_FROM_SAVING_BILL = "transfer from saving bill";
    private final SavingBillRepository savingBillRepository;
    private final SavingBillMapper mapper;
    private final BelongBillValidator belongBillValidator;
    private final MoneyTransactionService moneyTransactionService;


    public SavingBillReadDto create(SavingBillCreateDto savingBillCreateDto) {
        SavingBill savedBill = savingBillRepository.save(mapper.toEntity(savingBillCreateDto));
        return mapper.toDto(savedBill);
    }

    public SavingBillReadDto getById(Long id) {
        return savingBillRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.SAVING_BILL_NOT_FOUND));
    }

    public void disable(Long id) {
        savingBillRepository.findById(id)
                .ifPresentOrElse(
                        savingBillRepository::delete,
                        () -> {
                            throw new EntityNotFoundException(ResponseConstants.SAVING_BILL_NOT_FOUND);
                        }
                );
    }


    public MoneyTransactionReadDto doOperation(Long id, SavingBillOperationDto SavingBillOperationDto, OperationType operationType) {
        belongBillValidator.validateBills(SavingBillOperationDto.getMainBillId(), id);

        OperationType mainBillOpType = operationType.equals(OperationType.replenishment) ?
                OperationType.withdrawal : OperationType.replenishment;

        String description = operationType.equals(OperationType.replenishment) ?
                TRANSFER_TO_SAVING_BILL : TRANSFER_FROM_SAVING_BILL;

        MoneyTransactionCreateDto transaction = MoneyTransactionCreateDto.builder()
                .billId(SavingBillOperationDto.getMainBillId())
                .amount(SavingBillOperationDto.getAmount())
                .type(mainBillOpType)
                .description(description)
                .build();

        savingBillRepository.findById(id)
                .ifPresent((savingBill) -> changeSavingBillBalance(savingBill, operationType, SavingBillOperationDto.getAmount()));

        return moneyTransactionService.create(transaction);
    }

    private void changeSavingBillBalance(SavingBill savingBill, OperationType type, BigDecimal amount) {
        BigDecimal balance = savingBill.getBalance();
        if (type == OperationType.withdrawal) {
            if (balance.compareTo(amount) < 0)
                throw new NotEnoughBalance(ResponseConstants.NOT_ENOUGH_BALANCE);
            balance = balance.subtract(amount);
        } else {
            balance = balance.add(amount);
        }
        savingBill.setBalance(balance);
    }
}
