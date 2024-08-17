package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.requests.VerificationRequest;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.exceptions.VerificationException;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;

    private final DataRepository dataRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionEntity create(TransactionDto dto) {
        this.verifyBankAccountData(dto);

        TransactionEntity entityToSave = transactionMapper.convertDtoToEntity(dto);
        entityToSave.setStatus(TransactionStatus.SUCCESS); //Временно. TODO Поправить в зависимости от стадии
        entityToSave.setTransactionDate(LocalDateTime.now());

//        return transactionRepository.save(entityToSave);
        return null;
    }

    private void verifyBankAccountData(TransactionDto transactionDto) {
        log.debug("Verify sender id");
        if(!this.dataRepository.verifyBankAccount(transactionDto.getSenderId())) {
            throw new VerificationException(String.format("Sender account with id %s does not exist", transactionDto.getRecipientId()));
        }
        log.debug("Sender id is verified");

        log.debug("Verify recipient id");
        if(!this.dataRepository.verifyBankAccount(transactionDto.getRecipientId())) {
            throw new VerificationException(String.format("Recipient account with id %s does not exist", transactionDto.getRecipientId()));
        }
        log.debug("Recipient id is verified");
        log.info("All ids are verified");
    }
}
