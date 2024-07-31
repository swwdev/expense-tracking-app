package com.app.services.IT;

import com.app.dto.main_bill.MainBillCreateDto;
import com.app.exceptions.CantDeleteMainBillException;
import com.app.repositories.MainBillRepository;
import com.app.services.MainBillService;
import com.app.util.IntegrationTestBase;
import com.app.util.test_data.ExistedMainBillDataUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional

public class MainBillServiceIT extends IntegrationTestBase {
    private final MainBillCreateDto createDto = ExistedMainBillDataUtil.createMainBillCreateDto();
    @Autowired
    private MainBillRepository mainBillRepository;
    @Autowired
    private MainBillService mainBillService;

    @Test
    void getById() {
        assertAll(
                () -> assertThatNoException().isThrownBy(() -> mainBillService.getById(ExistedMainBillDataUtil.EXISTED_ID)),
                () -> assertThrows(EntityNotFoundException.class, () -> mainBillService.getById(ExistedMainBillDataUtil.NOT_EXISTED_ID))
        );
    }

    @Test
    void getWithTransactionsById() {
        assertAll(
                () -> assertThat(mainBillService.getWithTransactionsById(ExistedMainBillDataUtil.EXISTED_ID).getTransactions()).hasSize(5),
                () -> assertThrows(EntityNotFoundException.class, () -> mainBillService.getById(ExistedMainBillDataUtil.NOT_EXISTED_ID))
        );
    }

    @Test
    void create() {
        mainBillService.create(createDto);
        assertThat(mainBillRepository.findAll()).hasSize(7);
    }

    @Test
    void delete() {
        assertAll(
                () -> assertThrows(CantDeleteMainBillException.class, () -> mainBillService.delete(2L)),
                () -> {
                    mainBillService.delete(1L);
                    assertThat(mainBillRepository.findById(1L)).isNotPresent();
                }
        );
    }

    @Test
    void freeze() {
        mainBillService.freeze(ExistedMainBillDataUtil.EXISTED_ID);
        assertThat(mainBillRepository.findById(ExistedMainBillDataUtil.EXISTED_ID).get().getIsFrozen()).isTrue();
    }

    @Test
    void unfreeze() {
        mainBillService.unfreeze(ExistedMainBillDataUtil.EXISTED_ID);
        assertThat(mainBillRepository.findById(ExistedMainBillDataUtil.EXISTED_ID).get().getIsFrozen()).isFalse();
    }

}
