package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tsaccounts.entities.BankAccountEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
}
