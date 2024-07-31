package com.app.mapper;

import com.app.dto.saving_bill.SavingBillCreateDto;
import com.app.dto.saving_bill.SavingBillReadDto;
import com.app.models.SavingBill;
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
public interface SavingBillMapper {

    @Mapping(target = "openDate", expression = "java(MapperUtil.now())")
    @Mapping(target = "user", qualifiedByName = {"MapperUtil", "getUser"}, source = "userId")
    @Mapping(target = "balance", constant = "0")
    SavingBill toEntity(SavingBillCreateDto createDto);

    @Mapping(target = "target", source = "targetAmount")
    SavingBillReadDto toDto(SavingBill entity);
}
