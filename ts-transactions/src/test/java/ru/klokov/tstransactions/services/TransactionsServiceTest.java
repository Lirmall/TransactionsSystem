package ru.klokov.tstransactions.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.config.TestContainerConfExtension;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class TransactionsServiceTest {

    @Autowired
    private TransactionsService transactionsService;

    @Test
    void create() {
        TransactionDto dto = new TransactionDto();
        dto.setSenderId(1L);
        dto.setRecipientId(2L);
        dto.setAmount(100.0);
        dto.setType("Transfer");

        TransactionEntity result = transactionsService.create(dto);
        assertNotNull(result);

        assertNotNull(result.getId());
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getRecipientId());
        assertEquals(100.0, result.getAmount());
        assertEquals(TransactionStatus.SUCCESS, result.getStatusId());
        assertEquals(TransactionType.TRANSFER, result.getTypeId());
    }
}