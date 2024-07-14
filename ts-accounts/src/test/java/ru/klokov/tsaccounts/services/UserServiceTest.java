package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void create() {
    }

    @Test
    void findByIdTest() {
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> userService.findById(10000L));
    }
}