package com.app.mapper;

import com.app.dto.user.UserCreateDto;
import com.app.dto.user.UserReadDto;
import com.app.dto.user.UserUpdatePersonalInfoDto;
import com.app.dto.user.UserWithMainBillsReadDto;
import com.app.mapper.list_mappers.MainBillListMapper;
import com.app.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(
        uses = {
                MapperUtil.class, MainBillListMapper.class
        },
        imports = {
                LocalDateTime.class,
        },
        componentModel = MappingConstants.ComponentModel.SPRING,
        typeConversionPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserReadDto toDto(User user);

    @Mapping(target = "password", qualifiedByName = {"MapperUtil", "encode"}, source = "password")
    @Mapping(target = "registrationDate", expression = "java(MapperUtil.now())")
    @Mapping(target = "isActive", constant = "false")
    User toEntity(UserCreateDto user);


    UserWithMainBillsReadDto toDtoWithMainBills(User entityById);

    default User copyNotNullFields(UserUpdatePersonalInfoDto source, User target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getSurname() != null) {
            target.setSurname(source.getSurname());
        }
        if (source.getPatronymic() != null) {
            target.setPatronymic(source.getPatronymic());
        }
        return target;
    }
}
