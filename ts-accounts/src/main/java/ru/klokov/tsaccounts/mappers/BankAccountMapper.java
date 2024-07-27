package ru.klokov.tsaccounts.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.klokov.tsaccounts.dtos.BankAccountDto;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.models.UserModel;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {
    private final ModelMapper modelMapper;

    public BankAccountDto convertModelToDTO(BankAccountModel model) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(model, bankAccountDto);
        return bankAccountDto;
    }

    public BankAccountDto convertEntityToDTO(BankAccountEntity entity) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(entity, bankAccountDto);
        return bankAccountDto;
    }

    public BankAccountModel convertEntityToModel(BankAccountEntity entity) {
        BankAccountModel userModel = new BankAccountModel();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(entity, userModel);
        return userModel;
    }
}
