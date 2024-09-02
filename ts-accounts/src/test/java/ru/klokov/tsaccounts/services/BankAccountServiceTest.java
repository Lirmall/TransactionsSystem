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
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.specifications.bank_account.BankAccountSearchModel;
import ru.klokov.tsaccounts.test_objects.BankAccountSearchModelReturner;
import ru.klokov.tscommon.dtos.BankAccountBalanceVerificationDto;
import ru.klokov.tscommon.dtos.TransactionDataDto;

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
        Long ownerUserId = 7L;
        List<BankAccountModel> ownerUserAccounts = bankAccountService.findBankAccountsByOwnerUser(ownerUserId);
        assertEquals(2, ownerUserAccounts.size());

        BankAccountModel result = bankAccountService.create(ownerUserId);
        assertNotNull(result);

        List<BankAccountModel> ownerUserAccountsAfterCreate = bankAccountService.findBankAccountsByOwnerUser(ownerUserId);
        assertEquals(3, ownerUserAccountsAfterCreate.size());

        assertNotNull(result.getId());
        assertEquals(7L, result.getOwnerUserId());
        assertEquals(0.0, result.getBalance());
        assertFalse(result.getBlocked());
        assertFalse(result.getDeleted());
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
    void verifyBankAccountByIdTes() {
        assertTrue(() -> bankAccountService.verifyBankAccountById(1L));
        assertFalse(() -> bankAccountService.verifyBankAccountById(1000000000000000000L));
        assertFalse(() -> bankAccountService.verifyBankAccountById(4L));
        assertFalse(() -> bankAccountService.verifyBankAccountById(5L));
    }

    @Test
    void verifyBankAccountBalanceByIdTest() {
        assertTrue(() -> bankAccountService.verifyBankAccountBalanceById(new BankAccountBalanceVerificationDto(2L, 100.0)));
        assertFalse(() -> bankAccountService.verifyBankAccountBalanceById(new BankAccountBalanceVerificationDto(10L, 100.0)));

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

    @Transactional
    @Test
    void doTransactionTest() {
        Long senderId = 3L;
        Long recipientId = 11L;

        TransactionDataDto dto = new TransactionDataDto(senderId, recipientId, 200.0);

        BankAccountModel senderBeforeSend = bankAccountService.findById(senderId);
        BankAccountModel recipientBeforeSend = bankAccountService.findById(recipientId);

        assertEquals(12000.0, senderBeforeSend.getBalance());
        assertEquals(1220.0, recipientBeforeSend.getBalance());

        bankAccountService.doTransaction(dto);

        BankAccountModel senderAfterSend = bankAccountService.findById(senderId);
        BankAccountModel recipientAfterSend = bankAccountService.findById(recipientId);

        assertEquals(11800.0, senderAfterSend.getBalance());
        assertEquals(1420.0, recipientAfterSend.getBalance());
    }
}