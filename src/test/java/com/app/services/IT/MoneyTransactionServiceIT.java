package com.app.services.IT;

import com.app.dto.filtering.MoneyTransactionFilter;
import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.exceptions.FrozenBillException;
import com.app.exceptions.NotEnoughBalance;
import com.app.repositories.MainBillRepository;
import com.app.repositories.MoneyTransactionRepository;
import com.app.services.MoneyTransactionService;
import com.app.util.IntegrationTestBase;
import com.app.util.test_data.ExistedMoneyTransactionsDataUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class MoneyTransactionServiceIT extends IntegrationTestBase {
    private final MoneyTransactionCreateDto createDto = ExistedMoneyTransactionsDataUtil.createMoneyTransactionCreateDto();
    private final Pageable pageable = ExistedMoneyTransactionsDataUtil.getPageable();
    private final MoneyTransactionFilter filter = ExistedMoneyTransactionsDataUtil.getFilter();
    @Autowired
    MoneyTransactionService transactionService;
    @Autowired
    MainBillRepository mainBillRepository;
    @Autowired
    private MoneyTransactionRepository transactionRepository;

    @Test
    void create() {
        assertAll(
                () -> {
                    createDto.setBillId(ExistedMoneyTransactionsDataUtil.FROZEN_BILL_ID);
                    assertThrows(FrozenBillException.class, () -> transactionService.create(createDto));
                },
                () -> {
                    createDto.setBillId(ExistedMoneyTransactionsDataUtil.UNFROZEN_EMPTY_BILL_ID);
                    assertThrows(NotEnoughBalance.class, () -> transactionService.create(createDto));
                },
                () -> {
                    createDto.setBillId(ExistedMoneyTransactionsDataUtil.UNFROZEN_NOT_EMPTY_BILL_ID);
                    transactionService.create(createDto);
                    assertThat(transactionRepository.findAll()).hasSize(21);
                    assertThat(mainBillRepository.findById(ExistedMoneyTransactionsDataUtil.UNFROZEN_NOT_EMPTY_BILL_ID).get().getBalance().longValue())
                            .isEqualTo(1490L);
                }
        );
    }

    @Test
    void getById() {
        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> transactionService.getById(ExistedMoneyTransactionsDataUtil.NOT_EXISTED_ID)),
                () -> assertThatNoException().isThrownBy(() -> transactionService.getById(ExistedMoneyTransactionsDataUtil.EXISTED_ID))
        );
    }

    @Test
    void getByFilterWithUser() {
        assertThat(transactionService.getByFilterWithUser(filter, pageable))
                .hasSize(6);
    }

    @Test
    void getByFilterWithMainBill() {
        assertThat(transactionService.getByFilterWithMainBill(filter, pageable))
                .hasSize(5);
    }

}
