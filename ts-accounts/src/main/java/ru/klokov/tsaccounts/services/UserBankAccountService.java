package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.repositories.BankAccountRepository;
import ru.klokov.tsaccounts.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBankAccountService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public void blockBankAccountsByOwnerUserId(Long ownerUserID) {
        List<BankAccountEntity> entityList = bankAccountRepository.findBankAccountEntitiesByOwnerUserId(ownerUserID);
        for (BankAccountEntity bae : entityList) {
            bae.setBlocked(true);
            bankAccountRepository.save(bae);
        }
    }

    @Transactional(readOnly = true)
    public UserEntity findUserByBankAccountId(Long bankAccountId) {
        Optional<BankAccountEntity> foundAccount = bankAccountRepository.findById(bankAccountId);

        if(foundAccount.isEmpty()) {
            throw new NoMatchingEntryInDatabaseException(String.format("Bank account with id %s not found", bankAccountId));
        }

        BankAccountEntity bankAccount = foundAccount.get();

        Optional<UserEntity> foundUser = userRepository.findById(bankAccount.getOwnerUserId());

        if(foundUser.isEmpty()) {
            throw new NoMatchingEntryInDatabaseException(String.format("User with id %s not found", bankAccount.getOwnerUserId()));
        }

        return foundUser.get();
    }
}
