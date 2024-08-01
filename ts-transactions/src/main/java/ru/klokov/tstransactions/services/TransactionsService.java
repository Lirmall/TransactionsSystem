package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.dtos.CreateTransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionEntity create(CreateTransactionDto dto) {
        return null;
    }
}
