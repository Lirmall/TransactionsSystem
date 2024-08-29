package ru.klokov.tsaccounts.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tscommon.dtos.UserSimpleDataDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserEntityMapper {
    private final ModelMapper modelMapper;

    public UserDto convertModelToDTO(UserModel model) {
        return modelMapper.map(model, UserDto.class);
    }

    public UserDto convertEntityToDTO(UserEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    public UserModel convertEntityToModel(UserEntity entity) {
        return modelMapper.map(entity, UserModel.class);
    }

    public UserEntity convertModelToEntity(UserModel model) {
        return modelMapper.map(model, UserEntity.class);
    }

    public UserEntity convertDtoToEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserSimpleDataDto convertModelToSimpleDataDto(UserModel model, Set<Long> bankAccountIds) {
        UserSimpleDataDto dto = new UserSimpleDataDto();
        dto.setUsername(model.getUsername());
        dto.setUserId(model.getId());
        dto.setBankAccountIds(bankAccountIds);
        return dto;
    }
}
