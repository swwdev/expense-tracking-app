package com.app.controllers;

import com.app.dto.main_bill.MainBillCreateDto;
import com.app.dto.main_bill.MainBillReadDto;
import com.app.dto.main_bill.MainBillWithTransactionsReadDto;
import com.app.services.MainBillService;
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
import static com.app.utils.ResponseConstants.SUCCESSFULLY_FROZEN;
import static com.app.utils.ResponseConstants.SUCCESSFULLY_UNFROZEN;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main-bills")
public class MainBillController {
    private final MainBillService mainBillService;
    private final ValidationUserAccordingAuth validationUserAccordingAuth;


    @PostMapping
    public ResponseEntity<MainBillReadDto> create(@RequestBody @Validated MainBillCreateDto createDto, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(createDto.getUserId(), principal);
        return new ResponseEntity<>(mainBillService.create(createDto), CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainBillReadDto> get(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(id, principal);
        return ResponseEntity.ok().body(mainBillService.getById(id));
    }

    @GetMapping("/with-transactions/{id}")
    public ResponseEntity<MainBillWithTransactionsReadDto> getWithTransactions(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(id, principal);
        return ResponseEntity.ok().body(mainBillService.getWithTransactionsById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(id, principal);
        mainBillService.delete(id);
        return ResponseEntity.ok().body(SUCCESSFULLY_DELETED);
    }

    @PostMapping("/freeze/{id}")
    public ResponseEntity<String> freeze(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(id, principal);
        mainBillService.freeze(id);
        return ResponseEntity.ok().body(SUCCESSFULLY_FROZEN);
    }

    @PostMapping("/unfreeze/{id}")
    public ResponseEntity<String> unfreeze(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(id, principal);
        mainBillService.unfreeze(id);
        return ResponseEntity.ok().body(SUCCESSFULLY_UNFROZEN);
    }
}
