package com.app.mapper;

import com.app.dto.money_transaction.MoneyTransactionCreateDto;
import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.models.MoneyTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(
        uses = {
                MapperUtil.class
        },
        imports = {
                LocalDateTime.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        typeConversionPolicy = ReportingPolicy.IGNORE)
public interface MoneyTransactionMapper {

    @Mapping(target = "transactionDate", expression = "java(MapperUtil.now())")
    @Mapping(target = "mainBill", qualifiedByName = {"MapperUtil", "getMainBill"}, source = "billId")
    MoneyTransaction toEntity(MoneyTransactionCreateDto createDto);

    @Mapping(target = "completedIn", source = "transactionDate")
    MoneyTransactionReadDto toDto(MoneyTransaction entity);
}
