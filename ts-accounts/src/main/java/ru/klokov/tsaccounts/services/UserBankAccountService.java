package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.repositories.BankAccountRepository;
import ru.klokov.tsaccounts.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBankAccountService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public void blockBankAccountsByOwnerUserId(Long ownerUserID) {
        List<BankAccountEntity> entityList = bankAccountRepository.findBankAccountEntitiesByOwnerUserId(ownerUserID);
        for (BankAccountEntity bae : entityList) {
            bae.setBlocked(true);
            bankAccountRepository.save(bae);
        }
    }
}
