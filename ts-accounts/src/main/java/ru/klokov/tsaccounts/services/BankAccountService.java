package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.repositories.BankAccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public BankAccountEntity create(BankAccountEntity entity) {
        return bankAccountRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public BankAccountEntity findById(Long id) {
        Optional<BankAccountEntity> foundUser = bankAccountRepository.findById(id);

        if(foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new NoMatchingEntryInDatabaseException(String.format("Bank account wit id %s not found in database", id));
        }
    }
}
