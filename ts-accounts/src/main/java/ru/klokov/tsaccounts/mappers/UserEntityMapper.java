package ru.klokov.tsaccounts.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.models.UserModel;

@Component
@RequiredArgsConstructor
public class UserEntityMapper {
    private final ModelMapper modelMapper;

    public UserDto convertModelToDTO(UserModel model) {
        UserDto userDto = new UserDto();
        modelMapper.map(model, userDto);
        return userDto;
    }

    public UserDto convertEntityToDTO(UserEntity entity) {
        UserDto userDto = new UserDto();
        modelMapper.map(entity, userDto);
        return userDto;
    }

    public UserModel convertEntityToModel(UserEntity entity) {
        UserModel userModel = new UserModel();
        modelMapper.map(entity, userModel);
        return userModel;
    }

    public UserEntity convertModelToEntity(UserModel model) {
        UserEntity userEntity = new UserEntity();
        modelMapper.map(model, userEntity);
        return userEntity;
    }

    public UserEntity convertDtoToEntity(UserDto dto) {
        UserEntity userEntity = new UserEntity();
        modelMapper.map(dto, userEntity);
        return userEntity;
    }
}
