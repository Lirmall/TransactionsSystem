package ru.klokov.tstransactions.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tstransactions.entities.TransactionEntity;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}
