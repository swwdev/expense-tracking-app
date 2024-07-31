package com.app.mapper.list_mappers;

import com.app.dto.main_bill.MainBillReadDto;
import com.app.mapper.MainBillMapper;
import com.app.models.MainBill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        uses = {
                MainBillMapper.class
        },
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MainBillListMapper {
    List<MainBillReadDto> toDto(List<MainBill> entities);

}
