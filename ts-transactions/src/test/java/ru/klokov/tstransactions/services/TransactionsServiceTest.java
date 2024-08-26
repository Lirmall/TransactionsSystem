package ru.klokov.tstransactions.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tstransactions.config.TestContainerConfExtension;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static ru.klokov.tstransactions.entities.enums.TransactionStatus.SUCCESS;
import static ru.klokov.tstransactions.entities.enums.TransactionType.TRANSFER;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({TestContainerConfExtension.class, MockitoExtension.class})
class TransactionsServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    private TransactionsService transactionsService;

    @BeforeEach
    void init() {
        transactionsService = new TransactionsService(transactionRepository, dataRepository, transactionMapper);
    }

    @Test
    @Transactional
    void create() {
        TransactionDto dto = new TransactionDto();
        dto.setSenderId(1L);
        dto.setRecipientId(2L);
        dto.setAmount(100.0);
        dto.setType("Transfer");

        Mockito.when(dataRepository.verifyBankAccount(anyLong())).thenReturn(true);
        Mockito.when(dataRepository.checkBalanceForTransaction(anyLong(), anyDouble())).thenReturn(true);
        Mockito.when(dataRepository.doTransaction(ArgumentMatchers.any(TransactionDataDto.class))).thenReturn(true);

        TransactionModel createdTransaction = transactionsService.create(dto);

        TransactionEntity result = transactionMapper.convertModelToEntity(createdTransaction);
        assertNotNull(result);

        assertNotNull(result.getId());
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getRecipientId());
        assertEquals(100.0, result.getAmount());
        assertEquals(SUCCESS, result.getStatus());
        assertEquals(TRANSFER, result.getType());
    }

    @Test
    void findByIdTest() {
        TransactionModel model = transactionsService.findById(UUID.fromString("ac100d53-918f-12b7-8191-8fe2bd510000"));
        assertNotNull(model);

        TransactionEntity result = transactionMapper.convertModelToEntity(model);
        assertNotNull(result);

        assertEquals(UUID.fromString("ac100d53-918f-12b7-8191-8fe2bd510000"), result.getId());
        assertEquals(1, result.getSenderId());
        assertEquals(2, result.getRecipientId());
        assertEquals(200.0, result.getAmount());
        assertEquals(TRANSFER, result.getType());
        assertEquals(SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 7, 30, 12, 1, 3, 6000000), result.getTransactionDate());
    }
}