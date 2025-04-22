package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.exceptions.ExternalServiceUnavailableException;
import ru.klokov.tstransactions.exceptions.TransactionFailedException;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionManagementService {
    private final TransactionRepository transactionRepository;
    private final DataRepository dataRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionModel createTransaction(TransactionDto dto) {
        TransactionEntity entityToSave = transactionMapper.convertDtoToEntity(dto);
        entityToSave.setStatus(TransactionStatus.IN_PROGRESS);
        entityToSave.setTransactionDate(LocalDateTime.now());

        return transactionMapper.convertEntityToModel(transactionRepository.save(entityToSave));
    }

    @Transactional(noRollbackFor = TransactionFailedException.class)
    public TransactionModel completeTransaction(TransactionModel model) {
        TransactionDto dto = transactionMapper.convertModelToDto(model);
        boolean successful;

        Optional<TransactionEntity> savedEntity = transactionRepository.findById(model.getId());
        TransactionEntity transaction = savedEntity.orElseThrow(() -> new NoMatchingEntryInDatabaseException("Trouble with creating transaction"));

        try {
            verifyBankAccountData(dto);
            successful = dataRepository.doTransaction(new TransactionDataDto(dto.getSenderId(), dto.getRecipientId(), dto.getAmount()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            successful = false;
        }

        if (successful && transaction != null) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            return transactionMapper.convertEntityToModel(transactionRepository.save(transaction));
        } else {
            assert transaction != null;
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new TransactionFailedException(String.format("Transaction between bank account %s and bank account id %s failed", dto.getSenderId(), dto.getRecipientId()));
        }
    }

    private void verifyBankAccountData(TransactionDto transactionDto) {
        try {
            log.debug("Verify sender id");
            checkAccountExists(transactionDto.getSenderId());
            log.debug("Sender id is verified");

            log.debug("Verify recipient id");
            checkAccountExists(transactionDto.getRecipientId());
            log.debug("Recipient id is verified");

            log.info("All ids are verified");

            log.debug("Verify transaction amount");
            checkBalance(transactionDto);
            log.debug("Amount is verified");

            log.info("All transaction's data is verified");
        } catch (Exception ex) {
            throw new ExternalServiceUnavailableException("Unexpected error during transaction verification", ex);
        }
    }

    private void checkAccountExists(Long id) {
        if (!dataRepository.verifyBankAccount(id)) {
            throw new VerificationException(String.format("Recipient account with id %s does not exist",id));
        }
    }

    private void checkBalance(TransactionDto dto) {
        if (!dataRepository.checkBalanceForTransaction(dto.getSenderId(), dto.getAmount())) {
            throw new VerificationException("Not enough funds on sender account");
        }
    }
}
