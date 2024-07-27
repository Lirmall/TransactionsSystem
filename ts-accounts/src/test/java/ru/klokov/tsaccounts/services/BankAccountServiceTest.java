package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    @Transactional
    void create() {
        assertDoesNotThrow(() -> bankAccountService.create(1L));
    }

    @Test
    void findById() {
        assertDoesNotThrow(() -> bankAccountService.findById(1L));
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> bankAccountService.findById(10000L));
    }
}