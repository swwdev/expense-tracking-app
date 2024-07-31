package com.app.services;

import com.app.dto.main_bill.MainBillCreateDto;
import com.app.dto.main_bill.MainBillReadDto;
import com.app.dto.main_bill.MainBillWithTransactionsReadDto;
import com.app.exceptions.CantDeleteMainBillException;
import com.app.mapper.MainBillMapper;
import com.app.models.MainBill;
import com.app.repositories.MainBillRepository;
import com.app.utils.ResponseConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class MainBillService {

    private final MainBillMapper mainBillMapper;
    private final MainBillRepository mainBillRepository;

    public MainBillReadDto getById(Long id) {
        return mainBillMapper.toDto(getMainBill(id));
    }


    public MainBillWithTransactionsReadDto getWithTransactionsById(Long id) {
        return mainBillRepository.findWithTransactionsById(id)
                .map(mainBillMapper::toDtoWithTransactions)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.MAIN_BILL_NOT_FOUND));
    }

    public MainBillReadDto create(MainBillCreateDto createDto) {
        MainBill savedBill = mainBillRepository.save(mainBillMapper.toEntity(createDto));
        return mainBillMapper.toDto(savedBill);
    }

    public void delete(Long id) {
        MainBill bill = getMainBill(id);
        if (bill.getBalance().compareTo(BigDecimal.ZERO) > 0)
            throw new CantDeleteMainBillException(ResponseConstants.CANT_DELETE_BILL);
        else
            mainBillRepository.delete(bill);
    }

    public void freeze(Long id) {
        getMainBill(id).setIsFrozen(true);
    }

    public void unfreeze(Long id) {
        getMainBill(id).setIsFrozen(false);
    }

    private MainBill getMainBill(Long id) {
        return mainBillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ResponseConstants.MAIN_BILL_NOT_FOUND));
    }


}
