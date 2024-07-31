package com.app.services;

import com.app.dto.filtering.MoneyTransactionFilter;
import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.exceptions.FrozenBillException;
import com.app.exceptions.NotEnoughBalance;
import com.app.mapper.MoneyTransactionMapper;
import com.app.models.MainBill;
import com.app.models.MoneyTransaction;
import com.app.models.OperationType;
import com.app.repositories.MainBillRepository;
import com.app.repositories.MoneyTransactionRepository;
import com.app.utils.ResponseConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class MoneyTransactionService {
    private final MoneyTransactionMapper transactionMapper;
    private final MoneyTransactionRepository transactionRepository;
    private final MainBillRepository mainBillRepository;

    public MoneyTransactionReadDto create(MoneyTransactionCreateDto transactionCreateDto) {
        MainBill mainBill = mainBillRepository.findById(transactionCreateDto.getBillId()).get();
        if (mainBill.getIsFrozen())
            throw new FrozenBillException(ResponseConstants.FROZEN_BILL_EX_MSG);

        changeBillBalance(mainBill, transactionCreateDto.getType(), transactionCreateDto.getAmount());
        MoneyTransaction savedTransaction = transactionRepository.save(transactionMapper.toEntity(transactionCreateDto));
        return transactionMapper.toDto(savedTransaction);
    }

    public MoneyTransactionReadDto getById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.MONEY_TRANSACTION_NOT_FOUND));
    }

    public Page<MoneyTransactionReadDto> getByFilterWithUser(MoneyTransactionFilter filter, Pageable pageable) {
        return transactionRepository.findByFilterWithUser(
                        filter.getId(), filter.getFrom(), filter.getTo(), filter.getOperationType(), pageable
                )
                .map(transactionMapper::toDto);
    }

    public Page<MoneyTransactionReadDto> getByFilterWithMainBill(MoneyTransactionFilter filter, Pageable pageable) {
        return transactionRepository.findByFilterWithMainBill(
                        filter.getId(), filter.getFrom(), filter.getTo(), filter.getOperationType(), pageable
                )
                .map(transactionMapper::toDto);
    }

    private void changeBillBalance(MainBill mainBill, OperationType type, BigDecimal amount) {
        BigDecimal balance = mainBill.getBalance();
        if (type == OperationType.withdrawal) {
            if (balance.compareTo(amount) < 0)
                throw new NotEnoughBalance(ResponseConstants.NOT_ENOUGH_BALANCE);
            balance = balance.subtract(amount);
        } else {
            balance = balance.add(amount);
        }
        mainBill.setBalance(balance);
    }

}
