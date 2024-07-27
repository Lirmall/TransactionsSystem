package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tsaccounts.entities.BankAccountEntity;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    List<BankAccountEntity> findBankAccountEntitiesByOwnerUserId(Long ownerUserID);
}
