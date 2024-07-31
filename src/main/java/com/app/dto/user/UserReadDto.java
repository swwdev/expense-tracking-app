package com.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserReadDto {
    private Long id;

    private String name;

    private String surname;

    private String patronymic;

    private String email;

    private LocalDateTime registrationDate;

}
