package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.klokov.tsaccounts.entities.BankAccountEntity;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long>, JpaSpecificationExecutor<BankAccountEntity> {
    List<BankAccountEntity> findBankAccountEntitiesByOwnerUserId(Long ownerUserID);
}
