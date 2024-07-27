package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.mappers.BankAccountMapper;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.repositories.BankAccountRepository;
import ru.klokov.tsaccounts.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Transactional
    public BankAccountModel create(Long ownerUserId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(ownerUserId);

        if(optionalUserEntity.isEmpty()) {
            throw new NoMatchingEntryInDatabaseException(String.format("User with id %s is not found in the system, the account cannot be created", ownerUserId));
        }

        BankAccountEntity entityToSave = new BankAccountEntity();

        entityToSave.setOwnerUserId(ownerUserId);
        entityToSave.setBalance(0.0);

        return bankAccountMapper.convertEntityToModel(bankAccountRepository.save(entityToSave));
    }

    @Transactional(readOnly = true)
    public BankAccountModel findById(Long id) {
        Optional<BankAccountEntity> foundUser = bankAccountRepository.findById(id);

        if(foundUser.isPresent()) {
            return bankAccountMapper.convertEntityToModel(foundUser.get());
        } else {
            throw new NoMatchingEntryInDatabaseException(String.format("Bank account wit id %s not found in database", id));
        }
    }

    @Transactional(readOnly = true)
    public List<BankAccountModel> findBankAccountsByOwnerUser(Long ownerUserId) {
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findBankAccountEntitiesByOwnerUserId(ownerUserId);
        return bankAccountEntities.stream().map(bankAccountMapper::convertEntityToModel).toList();
    }
}