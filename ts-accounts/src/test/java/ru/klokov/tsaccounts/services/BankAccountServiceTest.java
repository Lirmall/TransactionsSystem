package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.dtos.BankAccountDto;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.exceptions.VerificationException;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.specifications.bank_account.BankAccountSearchModel;
import ru.klokov.tsaccounts.test_objects.BankAccountSearchModelReturner;
import ru.klokov.tsaccounts.test_utils.FieldsSouter;

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

    @Test
    void findByFilterWithCriteriaTest2() {
        BankAccountSearchModel modelWithBalanceMore = BankAccountSearchModelReturner.returnModelWithBalanceMore();

        Page<BankAccountDto> result = bankAccountService.findByFilterWithCriteria(modelWithBalanceMore);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        BankAccountDto dto1 = result.getContent().get(0);
        BankAccountDto dto2 = result.getContent().get(1);

        assertEquals(1, dto1.getId());
        assertEquals(1, dto1.getOwnerUserId());
        assertEquals(100000.0, dto1.getBalance());
        assertEquals(false, dto1.getBlocked());
        assertEquals(false, dto1.getDeleted());

        assertEquals(8, dto2.getId());
        assertEquals(5, dto2.getOwnerUserId());
        assertEquals(105000.0, dto2.getBalance());
        assertEquals(false, dto2.getBlocked());
        assertEquals(false, dto2.getDeleted());
    }

    @Test
    void findByFilterWithCriteriaTest3() {
        BankAccountSearchModel modelWithBalanceMoreAndDESCSort = BankAccountSearchModelReturner.returnModelWithBalanceMoreAndDESCSort();

        Page<BankAccountDto> result = bankAccountService.findByFilterWithCriteria(modelWithBalanceMoreAndDESCSort);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        BankAccountDto dto1 = result.getContent().get(0);
        BankAccountDto dto2 = result.getContent().get(1);

        assertEquals(8, dto1.getId());
        assertEquals(5, dto1.getOwnerUserId());
        assertEquals(105000.0, dto1.getBalance());
        assertEquals(false, dto1.getBlocked());
        assertEquals(false, dto1.getDeleted());

        assertEquals(1, dto2.getId());
        assertEquals(1, dto2.getOwnerUserId());
        assertEquals(100000.0, dto2.getBalance());
        assertEquals(false, dto2.getBlocked());
        assertEquals(false, dto2.getDeleted());
    }

    @Test
    void findByFilterWithCriteriaThrowValidExceptionTest() {
        BankAccountSearchModel modelWithWrongSortField = BankAccountSearchModelReturner.returnModelWithWrongSortField();

        assertThrows(VerificationException.class, () -> bankAccountService.findByFilterWithCriteria(modelWithWrongSortField));
    }
}