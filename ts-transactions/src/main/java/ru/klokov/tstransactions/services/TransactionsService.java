package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tstransactions.dtos.soap.SoapTransactionRequestDto;
import ru.klokov.tstransactions.dtos.soap.SoapTransactionResponseDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
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

    public TransactionModel create(TransactionDto dto) {
        TransactionModel transaction = transactionManagementService.createTransaction(dto);
        return transactionManagementService.completeTransaction(transaction);
    }

    public SoapTransactionResponseDto soapCreateTransaction(SoapTransactionRequestDto dto) {
        TransactionDto transactionDto = transactionMapper.convertSoapRequestToDto(dto);
        TransactionModel transaction = transactionManagementService.createTransaction(transactionDto);
        TransactionModel completed =  transactionManagementService.completeTransaction(transaction);
        return transactionMapper.convertModelToSoaResponseDto(completed);
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
