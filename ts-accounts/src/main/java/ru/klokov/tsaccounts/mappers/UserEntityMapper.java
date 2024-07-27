package ru.klokov.tsaccounts.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(model, userDto);
        return userDto;
    }

    public UserDto convertEntityToDTO(UserEntity entity) {
        UserDto userDto = new UserDto();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(entity, userDto);
        return userDto;
    }

    public UserModel convertEntityToModel(UserEntity entity) {
        UserModel userModel = new UserModel();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(entity, userModel);
        return userModel;
    }
}
