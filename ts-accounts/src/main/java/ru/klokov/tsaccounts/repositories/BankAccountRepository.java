package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.klokov.tsaccounts.entities.BankAccountEntity;

import java.util.List;
import java.util.Set;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long>, JpaSpecificationExecutor<BankAccountEntity> {

    List<BankAccountEntity> findBankAccountEntitiesByOwnerUserId(Long ownerUserID);


    @Query(value = "select ba.id from accounts.bank_accounts ba where ba.owner_user = :ownerUserId", nativeQuery = true)
    Set<Long> findBankAccountIdsByOwnerUserId(Long ownerUserId);
}
