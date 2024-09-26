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
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tstransactions.config.TestContainerConfExtension;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.models.TransactionModel;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;
import ru.klokov.tstransactions.specifications.TransactionSearchModel;
import ru.klokov.tstransactions.specifications.sort.TransactionSortChecker;
import ru.klokov.tstransactions.test_objects.TransactionSearchModelReturner;

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

    @Autowired
    private TransactionSortChecker transactionSortChecker;

    private TransactionsService transactionsService;

    @BeforeEach
    void init() {
        transactionsService = new TransactionsService(transactionRepository, dataRepository, transactionMapper, transactionSortChecker);
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

    @Test
    void findByFilterWithCriteriaTest() {
        TransactionSearchModel modelWithOneId = TransactionSearchModelReturner.returnModelWithOneId();

        Page<TransactionDto> result = transactionsService.findByFilterWithCriteria(modelWithOneId);
        assertNotNull(result);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByFilterWithTwoDatesTest() {
        TransactionSearchModel modelWithTwoDates = TransactionSearchModelReturner.returnModelWithTwoDates();

        Page<TransactionDto> result = transactionsService.findByFilterWithCriteria(modelWithTwoDates);
        assertNotNull(result);

        assertEquals(3, result.getTotalElements());

        TransactionDto dto1 = result.getContent().get(0);
        TransactionDto dto2 = result.getContent().get(1);
        TransactionDto dto3 = result.getContent().get(2);

        assertEquals(UUID.fromString("7f21cfbb-4919-4e39-a6af-23005a878132"), dto1.getId());
        assertEquals(6, dto1.getSenderId());
        assertEquals(4, dto1.getRecipientId());
        assertEquals(6431.3, dto1.getAmount());
        assertEquals("Deposit", dto1.getType());
        assertEquals("Success", dto1.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 28, 23, 11, 4, 366859000), dto1.getTransactionDate());

        assertEquals(UUID.fromString("68931356-6783-4424-a0f5-6556efafad72"), dto2.getId());
        assertEquals(3, dto2.getSenderId());
        assertEquals(5, dto2.getRecipientId());
        assertEquals(108.95, dto2.getAmount());
        assertEquals("Transfer", dto2.getType());
        assertEquals("In Progress", dto2.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 28, 13, 59, 25, 20368000), dto2.getTransactionDate());

        assertEquals(UUID.fromString("92438179-2a53-4a15-bc9a-5b741454e729"), dto3.getId());
        assertEquals(2, dto3.getSenderId());
        assertEquals(4, dto3.getRecipientId());
        assertEquals(648.73, dto3.getAmount());
        assertEquals("Cash Withdrawal", dto3.getType());
        assertEquals("Failed", dto3.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 27, 13, 1, 17, 327295000), dto3.getTransactionDate());
    }

    @Test
    void findByFilterWithTwoDatesTest2() {
        TransactionSearchModel modelWithTwoDates = TransactionSearchModelReturner.returnModelWithTwoDates();
        modelWithTwoDates.setSortColumn("transactionDate");

        Page<TransactionDto> result = transactionsService.findByFilterWithCriteria(modelWithTwoDates);
        assertNotNull(result);

        assertEquals(3, result.getTotalElements());

        TransactionDto dto1 = result.getContent().get(0);
        TransactionDto dto2 = result.getContent().get(1);
        TransactionDto dto3 = result.getContent().get(2);

        assertEquals(UUID.fromString("92438179-2a53-4a15-bc9a-5b741454e729"), dto1.getId());
        assertEquals(2, dto1.getSenderId());
        assertEquals(4, dto1.getRecipientId());
        assertEquals(648.73, dto1.getAmount());
        assertEquals("Cash Withdrawal", dto1.getType());
        assertEquals("Failed", dto1.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 27, 13, 1, 17, 327295000), dto1.getTransactionDate());

        assertEquals(UUID.fromString("68931356-6783-4424-a0f5-6556efafad72"), dto2.getId());
        assertEquals(3, dto2.getSenderId());
        assertEquals(5, dto2.getRecipientId());
        assertEquals(108.95, dto2.getAmount());
        assertEquals("Transfer", dto2.getType());
        assertEquals("In Progress", dto2.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 28, 13, 59, 25, 20368000), dto2.getTransactionDate());

        assertEquals(UUID.fromString("7f21cfbb-4919-4e39-a6af-23005a878132"), dto3.getId());
        assertEquals(6, dto3.getSenderId());
        assertEquals(4, dto3.getRecipientId());
        assertEquals(6431.3, dto3.getAmount());
        assertEquals("Deposit", dto3.getType());
        assertEquals("Success", dto3.getStatus());
        assertEquals(LocalDateTime.of(2024, 8, 28, 23, 11, 4, 366859000), dto3.getTransactionDate());
    }
}