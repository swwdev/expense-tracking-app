package com.app.mapper;

import com.app.dto.main_bill.MainBillCreateDto;
import com.app.dto.main_bill.MainBillReadDto;
import com.app.dto.main_bill.MainBillWithTransactionsReadDto;
import com.app.mapper.list_mappers.MoneyTransactionsListMapper;
import com.app.models.MainBill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(
        uses = {
                MapperUtil.class, MoneyTransactionsListMapper.class
        },
        imports = {
                LocalDateTime.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        typeConversionPolicy = ReportingPolicy.IGNORE)
public interface MainBillMapper {

    @Mapping(target = "openDate", expression = "java(MapperUtil.now())")
    @Mapping(target = "user", qualifiedByName = {"MapperUtil", "getUser"}, source = "userId")
    @Mapping(target = "balance", source = "initialBalance")
    @Mapping(target = "isFrozen", constant = "false")
    MainBill toEntity(MainBillCreateDto createDto);

    MainBillReadDto toDto(MainBill mainBill);


    MainBillWithTransactionsReadDto toDtoWithTransactions(MainBill mainBill);
}
