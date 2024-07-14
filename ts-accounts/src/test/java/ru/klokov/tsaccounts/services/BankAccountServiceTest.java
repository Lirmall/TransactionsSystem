package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    void create() {
    }

    @Test
    void findById() {
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> bankAccountService.findById(10000L));
    }
}