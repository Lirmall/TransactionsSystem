package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.dtos.CreateTransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionEntity create(CreateTransactionDto dto) {
        TransactionEntity entityToSave = new TransactionEntity();
        entityToSave.setSenderId(dto.getSenderId());
        entityToSave.setRecipientId(dto.getRecipientId());
        entityToSave.setAmount(dto.getAmount());
        entityToSave.setTypeId(dto.getTypeId());
        entityToSave.setStatusId(1L); //Временно. 1L - успешно. TODO Поправить в зависимости от стадии
        entityToSave.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(entityToSave);
    }
}
