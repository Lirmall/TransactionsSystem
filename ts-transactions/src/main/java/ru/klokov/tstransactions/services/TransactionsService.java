package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionEntity create(TransactionDto dto) {
        TransactionEntity entityToSave = transactionMapper.convertDtoToEntity(dto);
        entityToSave.setStatus(TransactionStatus.SUCCESS); //Временно. TODO Поправить в зависимости от стадии
        entityToSave.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(entityToSave);
    }
}
