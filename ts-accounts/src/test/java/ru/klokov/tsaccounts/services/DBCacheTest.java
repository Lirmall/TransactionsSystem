package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.repositories.BankAccountRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(TestContainerConfExtension.class)
public class DBCacheTest {
    //Это тест для проверки работы кэша 2 и 3 уровня. Проверка осуществляется по логу вывода, ассерты для вида

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void secondLevelCacheTest() {
        BankAccountModel bankAccountModel = bankAccountService.findById(1L);
        Optional<BankAccountEntity> bankAccountModel2 = bankAccountRepository.findById(1L);
        BankAccountModel bankAccountModel3 = bankAccountService.findById(1L);
    }

    @Test
    void thirdLevelCacheTest() {
        List<BankAccountModel> models = bankAccountService.findBankAccountsByOwnerUser(1L);
        List<BankAccountModel> models2 = bankAccountService.findBankAccountsByOwnerUser(1L);
        List<BankAccountModel> models3 = bankAccountService.findBankAccountsByOwnerUser(1L);

    }
}
