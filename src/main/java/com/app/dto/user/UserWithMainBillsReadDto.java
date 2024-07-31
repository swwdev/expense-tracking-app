package com.app.dto.user;

import com.app.dto.main_bill.MainBillReadDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserWithMainBillsReadDto {
    private Long id;

    private String name;

    private String surname;

    private String patronymic;

    private String email;

    private LocalDateTime registrationDate;

    private List<MainBillReadDto> mainBills;
}
