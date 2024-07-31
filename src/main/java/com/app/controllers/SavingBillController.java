package com.app.controllers;

import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.dto.saving_bill.SavingBillCreateDto;
import com.app.dto.saving_bill.SavingBillOperationDto;
import com.app.dto.saving_bill.SavingBillReadDto;
import com.app.models.OperationType;
import com.app.services.SavingBillService;
import com.app.services.ValidationUserAccordingAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.app.utils.ResponseConstants.SUCCESSFULLY_DELETED;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/saving-bills")
@RequiredArgsConstructor
public class SavingBillController {

    private final SavingBillService savingBillService;
    private final ValidationUserAccordingAuth validationUserAccordingAuth;

    @PostMapping
    public ResponseEntity<SavingBillReadDto> save(@RequestBody @Validated SavingBillCreateDto savingBillCreateDto,
                                                  Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(savingBillCreateDto.getUserId(), principal);
        return new ResponseEntity<>(savingBillService.create(savingBillCreateDto), CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingBillReadDto> get(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainSavingBill(id, principal);
        return ResponseEntity.ok().body(savingBillService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> disableBill(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainSavingBill(id, principal);
        savingBillService.disable(id);
        return ResponseEntity.ok().body(SUCCESSFULLY_DELETED);
    }

    @PostMapping("top-up/{id}")
    public ResponseEntity<MoneyTransactionReadDto> topUP(@PathVariable Long id,
                                                         @RequestBody @Validated SavingBillOperationDto SavingBillOperationDto,
                                                         Principal principal) {
        validationUserAccordingAuth.validateUserObtainSavingBill(id, principal);
        return ResponseEntity.ok().body(savingBillService.doOperation(id, SavingBillOperationDto, OperationType.replenishment));
    }

    @PostMapping("withdraw/{id}")
    public ResponseEntity<MoneyTransactionReadDto> withdraw(@PathVariable Long id,
                                                            @RequestBody @Validated SavingBillOperationDto SavingBillOperationDto,
                                                            Principal principal) {
        validationUserAccordingAuth.validateUserObtainSavingBill(id, principal);
        return ResponseEntity.ok().body(savingBillService.doOperation(id, SavingBillOperationDto, OperationType.withdrawal));
    }
}
