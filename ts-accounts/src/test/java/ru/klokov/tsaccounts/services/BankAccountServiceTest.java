package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.dtos.BankAccountDto;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.specifications.bank_account.BankAccountSearchModel;
import ru.klokov.tsaccounts.test_objects.BankAccountSearchModelReturner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    @Transactional
    void createTest() {
        assertDoesNotThrow(() -> bankAccountService.create(1L));
    }

    @Test
    @Transactional
    void createThrowExceptionTest() {
        Long nonExistingOwnerUserId = 100000000000L;
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> bankAccountService.create(nonExistingOwnerUserId));
    }

    @Test
    void findByIdTest() {
        assertDoesNotThrow(() -> bankAccountService.findById(1L));
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> bankAccountService.findById(10000L));
    }

    @Test
    void findByOwnerUserIdTest() {
        Long ownerUserWithThreeBankAccountsId = 5L;
        List<BankAccountModel> bankAccountModels = bankAccountService.findBankAccountsByOwnerUser(ownerUserWithThreeBankAccountsId);

        assertNotNull(bankAccountModels);
        assertEquals(3, bankAccountModels.size());
    }

    @Test
    void findByFilterWithCriteriaTest1() {
        BankAccountSearchModel modelWithOneId = BankAccountSearchModelReturner.returnModelWithOneId();

        Page<BankAccountDto> result = bankAccountService.findByFilterWithCriteria(modelWithOneId);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}