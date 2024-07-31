package com.app.mapper.list_mappers;

import com.app.dto.money_transaction.MoneyTransactionReadDto;
import com.app.mapper.MoneyTransactionMapper;
import com.app.models.MoneyTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        uses = {
                MoneyTransactionMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyTransactionsListMapper {
    List<MoneyTransactionReadDto> toDto(List<MoneyTransaction> entities);

}
