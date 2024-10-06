package ru.klokov.tsaccounts.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tscommon.dtos.BankAccountDto;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.models.BankAccountModel;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {
    private final ModelMapper modelMapper;

    public BankAccountDto convertModelToDTO(BankAccountModel model) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        modelMapper.map(model, bankAccountDto);
        return bankAccountDto;
    }

    public BankAccountDto convertEntityToDTO(BankAccountEntity entity) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        modelMapper.map(entity, bankAccountDto);
        return bankAccountDto;
    }

    public BankAccountModel convertEntityToModel(BankAccountEntity entity) {
        BankAccountModel userModel = new BankAccountModel();
        modelMapper.map(entity, userModel);
        return userModel;
    }
}
