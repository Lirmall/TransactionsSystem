package ru.klokov.tsaccounts.repositories;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ru.klokov.tsaccounts.entities.BankAccountEntity;

import java.util.List;
import java.util.Set;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long>, JpaSpecificationExecutor<BankAccountEntity> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    List<BankAccountEntity> findBankAccountEntitiesByOwnerUserId(Long ownerUserID);


    @Query(value = "select ba.id from accounts.bank_accounts ba where ba.owner_user = :ownerUserId", nativeQuery = true)
    Set<Long> findBankAccountIdsByOwnerUserId(Long ownerUserId);
}
