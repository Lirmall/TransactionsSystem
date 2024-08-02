package ru.klokov.tstransactions.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.config.TestContainerConfExtension;
import ru.klokov.tstransactions.dtos.CreateTransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;

import java.util.UUID;

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
        CreateTransactionDto dto = new CreateTransactionDto();
        dto.setSenderId(1L);
        dto.setRecipientId(2L);
        dto.setAmount(100.0);
        dto.setTypeId(4L);

        TransactionEntity result = transactionsService.create(dto);
        assertNotNull(result);

        assertNotNull(result.getId());
    }
}