package ru.klokov.tstransactions.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.exceptions.TransactionFailedException;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;
import ru.klokov.tstransactions.specifications.TransactionSpecificationBuilder;
import ru.klokov.tstransactions.specifications.sort.TransactionSortChecker;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionSortChecker sortChecker;
    private final TransactionManagementService transactionManagementService;

//    @Transactional
    public TransactionModel create(TransactionDto dto) {
        TransactionModel transaction = transactionManagementService.createTransaction(dto);
        return transactionManagementService.completeTransaction(transaction);
    }

//    private TransactionModel privateCreate(TransactionDto dto) {
//        verifyBankAccountData(dto);
//
//        TransactionEntity entityToSave = transactionMapper.convertDtoToEntity(dto);
//        entityToSave.setStatus(TransactionStatus.IN_PROGRESS);
//        entityToSave.setTransactionDate(LocalDateTime.now());
//
//        UUID transactionId = transactionRepository.save(entityToSave).getId();
//        entityManager.flush();
//        entityManager.clear();
//
//        boolean successful = dataRepository.doTransaction(new TransactionDataDto(dto.getSenderId(), dto.getRecipientId(), dto.getAmount()));
//        Optional<TransactionEntity> savedEntity = transactionRepository.findById(transactionId);
//
//        TransactionEntity transaction = savedEntity.orElseThrow(() -> new NoMatchingEntryInDatabaseException("Trouble with creating transaction"));
//
//        if (successful) {
//            transaction.setStatus(TransactionStatus.SUCCESS);
//            return transactionMapper.convertEntityToModel(transactionRepository.save(transaction));
//        } else {
//            transaction.setStatus(TransactionStatus.FAILED);
//            log.error("Transaction between bank account {} and bank account id {} failed", dto.getSenderId(), dto.getRecipientId());
//            throw new TransactionFailedException(String.format("Transaction between bank account %s and bank account id %s failed", dto.getSenderId(), dto.getRecipientId()));
//        }
//    }



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
    public Page<TransactionDto> findByFilterWithCriteria(TransactionSearchModel searchModel) {
        PageRequest pageable = sortChecker.getPageableAndSort(searchModel);
        searchModel.getCriteriaList().forEach(criteria -> sortChecker.columnCheck(criteria.getFieldName()));

        if (!searchModel.getCriteriaList().isEmpty()) {
            TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder(searchModel.getCriteriaList());
            Page<TransactionEntity> entities = transactionRepository.findAll(builder.build(), pageable);
            log.info("entities size: {}", entities.getTotalElements());
            log.info("entities total: {}", entities.getTotalPages());

            Page<TransactionDto> dtoPage = entities.map(transactionMapper::convertEntityToDto);
            return dtoPage;
        } else {
            return transactionRepository.findAll(pageable).map(transactionMapper::convertEntityToDto);
        }
    }

//    private void verifyBankAccountData(TransactionDto transactionDto) {
//        log.debug("Verify sender id");
//        if (!dataRepository.verifyBankAccount(transactionDto.getSenderId())) {
//            throw new VerificationException(String.format("Sender account with id %s does not exist", transactionDto.getRecipientId()));
//        }
//        log.debug("Sender id is verified");
//
//        log.debug("Verify recipient id");
//        if (!dataRepository.verifyBankAccount(transactionDto.getRecipientId())) {
//            throw new VerificationException(String.format("Recipient account with id %s does not exist", transactionDto.getRecipientId()));
//        }
//        log.debug("Recipient id is verified");
//
//        log.info("All ids are verified");
//
//        log.debug("Verify transaction amount");
//        if (!dataRepository.checkBalanceForTransaction(transactionDto.getSenderId(), transactionDto.getAmount())) {
//            throw new VerificationException(String.format("Recipient account with id %s doesn't have enough funds on balance", transactionDto.getRecipientId()));
//        }
//        log.debug("Amount is verified");
//
//        log.info("All transaction's data is verified");
//    }

    @Transactional
    public void clearTransactions() {
        transactionRepository.truncateTransactions();
    }

    @Transactional
    public TransactionModel refund(UUID transactionId) {
        if (transactionId == null) {
            throw new RuntimeException("Transaction id is null");
        }

        Optional<TransactionEntity> transactionToRefund = transactionRepository.findById(transactionId);
        TransactionEntity entityToRefund = transactionToRefund.orElseThrow(() -> new NoMatchingEntryInDatabaseException("Transaction with these parameters not found"));

        checkRefundParameters(entityToRefund);

        TransactionDto refundDto = transactionMapper.convertEntityToDto(entityToRefund);
        refundDto.setId(null);
        refundDto.setRecipientId(entityToRefund.getSenderId());
        refundDto.setSenderId(entityToRefund.getRecipientId());
        refundDto.setTypeId(TransactionType.REFUND.getId());
        refundDto.setType(TransactionType.REFUND.getName());

        TransactionModel transaction = transactionManagementService.createTransaction(refundDto);
        return transactionManagementService.completeTransaction(transaction);
    }

    private static void checkRefundParameters(TransactionEntity entityToRefund) {
        if (!entityToRefund.isRefundable() || entityToRefund.getTransactionDate().isAfter(LocalDateTime.now()) || !entityToRefund.isCompleted()) {
            throw new RuntimeException("Cannot refund transaction with these parameters");
        }
    }
}
