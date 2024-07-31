package com.app.services.IT;

import com.app.dto.saving_bill.SavingBillCreateDto;
import com.app.dto.saving_bill.SavingBillOperationDto;
import com.app.exceptions.InvalidMainBillException;
import com.app.exceptions.NotEnoughBalance;
import com.app.models.OperationType;
import com.app.repositories.SavingBillRepository;
import com.app.services.SavingBillService;
import com.app.util.IntegrationTestBase;
import com.app.util.test_data.ExistedSavingBillDataUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class SavingBillServiceIT extends IntegrationTestBase {

    private final SavingBillCreateDto createDto = ExistedSavingBillDataUtil.createSavingBillCreateDto();
    private final SavingBillOperationDto operationDto = ExistedSavingBillDataUtil.createOperationDto();
    @Autowired
    private SavingBillRepository savingBillRepository;
    @Autowired
    private SavingBillService savingBillService;

    @Test
    void create() {
        savingBillService.create(createDto);
        assertThat(savingBillRepository.findAll()).hasSize(6);
    }

    @Test
    void getById() {
        assertAll(
                () -> assertThatNoException().isThrownBy(() -> savingBillService.getById(ExistedSavingBillDataUtil.EXISTED_ID)),
                () -> assertThrows(EntityNotFoundException.class, () -> savingBillService.getById(ExistedSavingBillDataUtil.NOT_EXISTED_ID))
        );
    }

    @Test
    void disable() {
        assertAll(
                () -> assertThatNoException().isThrownBy(() -> savingBillService.disable(ExistedSavingBillDataUtil.EXISTED_ID)),
                () -> assertThrows(EntityNotFoundException.class, () -> savingBillService.disable(ExistedSavingBillDataUtil.NOT_EXISTED_ID))
        );
    }

    @Test
    void doOperation() {
        assertAll(
                () -> assertThrows(EntityNotFoundException.class,
                        () -> savingBillService.doOperation(ExistedSavingBillDataUtil.NOT_EXISTED_ID, operationDto, OperationType.withdrawal)),
                () -> {
                    operationDto.setMainBillId(5L);
                    assertThrows(InvalidMainBillException.class,
                            () -> savingBillService.doOperation(ExistedSavingBillDataUtil.EXISTED_ID, operationDto, OperationType.withdrawal));
                },
                () -> {
                    operationDto.setMainBillId(5L);
                    assertThrows(NotEnoughBalance.class,
                            () -> savingBillService.doOperation(4L, operationDto, OperationType.withdrawal));
                },
                () -> {
                    operationDto.setMainBillId(1L);
                    assertThatNoException().isThrownBy(
                            () -> savingBillService.doOperation(ExistedSavingBillDataUtil.EXISTED_ID, operationDto, OperationType.withdrawal));
                }
        );
    }
}
