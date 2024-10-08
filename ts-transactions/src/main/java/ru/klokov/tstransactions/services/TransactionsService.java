package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.exceptions.TransactionFailedException;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tstransactions.specifications.TransactionSpecificationBuilder;
import ru.klokov.tstransactions.specifications.sort.TransactionSortChecker;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final DataRepository dataRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionSortChecker sortChecker;

    @Transactional
    public TransactionModel create(TransactionDto dto) {
        verifyBankAccountData(dto);

        TransactionEntity entityToSave = transactionMapper.convertDtoToEntity(dto);
        entityToSave.setStatus(TransactionStatus.SUCCESS); //Временно. TODO Поправить в зависимости от стадии
        entityToSave.setTransactionDate(LocalDateTime.now());

        boolean successful = dataRepository.doTransaction(new TransactionDataDto(dto.getSenderId(), dto.getRecipientId(), dto.getAmount()));
        if (successful) {
            return transactionMapper.convertEntityToModel(transactionRepository.save(entityToSave));
        } else {
            log.error("Transaction between bank account {} and bank account id {} failed", dto.getSenderId(), dto.getRecipientId());
            throw new TransactionFailedException(String.format("Transaction between bank account %s and bank account id %s failed", dto.getSenderId(), dto.getRecipientId()));
        }
    }

    @Transactional(readOnly = true)
    public TransactionModel findById(UUID id) {
        return transactionMapper.convertEntityToModel(privateFindById(id));
    }

    private TransactionEntity privateFindById(UUID id) {
        Optional<TransactionEntity> foundTransaction = transactionRepository.findById(id);
        if (foundTransaction.isPresent()) {
            return foundTransaction.get();
        } else {
            throw new NoMatchingEntryInDatabaseException("Transaction with these parameters not found");
        }
    }

    @Transactional
    public PageImpl<TransactionDto> findByFilterWithCriteria(TransactionSearchModel searchModel) {
        PageRequest pageable = sortChecker.getPageableAndSort(searchModel);

        if(!searchModel.getCriteriaList().isEmpty()) {
            TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder(searchModel.getCriteriaList());
            Page<TransactionEntity> entities = transactionRepository.findAll(builder.build(), pageable);
            List<TransactionDto> dtos = entities.getContent().stream().map(transactionMapper::convertEntityToDto).toList();

            return new PageImpl<>(dtos, pageable, dtos.size());
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    private void verifyBankAccountData(TransactionDto transactionDto) {
        log.debug("Verify sender id");
        if (!dataRepository.verifyBankAccount(transactionDto.getSenderId())) {
            throw new VerificationException(String.format("Sender account with id %s does not exist", transactionDto.getRecipientId()));
        }
        log.debug("Sender id is verified");

        log.debug("Verify recipient id");
        if (!dataRepository.verifyBankAccount(transactionDto.getRecipientId())) {
            throw new VerificationException(String.format("Recipient account with id %s does not exist", transactionDto.getRecipientId()));
        }
        log.debug("Recipient id is verified");

        log.info("All ids are verified");

        log.debug("Verify transaction amount");
        if (!dataRepository.checkBalanceForTransaction(transactionDto.getSenderId(), transactionDto.getAmount())) {
            throw new VerificationException(String.format("Recipient account with id %s doesn't have enough funds on balance", transactionDto.getRecipientId()));
        }
        log.debug("Amount is verified");

        log.info("All transaction's data is verified");
    }
}
