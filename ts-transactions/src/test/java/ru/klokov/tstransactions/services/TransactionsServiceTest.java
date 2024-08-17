package ru.klokov.tstransactions.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.config.TestContainerConfExtension;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.repositories.DataRepository;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

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

        TransactionEntity result = transactionsService.create(dto);
        assertNotNull(result);

//        assertNotNull(result.getId()); //Временно. TODO Раскомментировать после добавления функционала в модуль аккаунтов
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getRecipientId());
        assertEquals(100.0, result.getAmount());
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        assertEquals(TransactionType.TRANSFER, result.getType());
    }
}