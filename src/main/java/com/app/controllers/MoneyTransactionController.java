package com.app.controllers;

import com.app.dto.filtering.MoneyTransactionFilter;
import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.dto.response.PageResponse;
import com.app.services.MoneyTransactionService;
import com.app.services.ValidationUserAccordingAuth;
import com.app.validation.groups.MainBillValidation;
import com.app.validation.groups.UserValidation;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class MoneyTransactionController {
    private final MoneyTransactionService moneyTransactionService;
    private final ValidationUserAccordingAuth validationUserAccordingAuth;

    @GetMapping("/{id}")
    public ResponseEntity<MoneyTransactionReadDto> get(@PathVariable Long id, Principal principal) {
        validationUserAccordingAuth.validateUserObtainTransaction(id, principal);
        return ResponseEntity.ok(moneyTransactionService.getById(id));
    }

    @GetMapping("/with-user")
    public ResponseEntity<PageResponse<MoneyTransactionReadDto>> getWithUser
            (@RequestBody @Validated({UserValidation.class, Default.class}) MoneyTransactionFilter filter,
             Pageable pageable, Principal principal) {
        validationUserAccordingAuth.validateUserObtainUser(filter.getId(), principal);
        Page<MoneyTransactionReadDto> page = moneyTransactionService.getByFilterWithUser(filter, pageable);
        return ResponseEntity.ok().body(PageResponse.of(page));
    }

    @GetMapping("/with-main-bill")
    public ResponseEntity<PageResponse<MoneyTransactionReadDto>> getWithMainBill
            (@RequestBody @Validated({MainBillValidation.class, Default.class}) MoneyTransactionFilter filter,
             Pageable pageable, Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(filter.getId(), principal);
        Page<MoneyTransactionReadDto> page = moneyTransactionService.getByFilterWithMainBill(filter, pageable);
        return ResponseEntity.ok().body(PageResponse.of(page));
    }


    @PostMapping
    ResponseEntity<MoneyTransactionReadDto> create(@RequestBody @Validated MoneyTransactionCreateDto createDto,
                                                   Principal principal) {
        validationUserAccordingAuth.validateUserObtainThisMainBill(createDto.getBillId(), principal);
        return new ResponseEntity<>(moneyTransactionService.create(createDto), CREATED);
    }
}
