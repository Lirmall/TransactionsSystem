package ru.klokov.tsaccounts.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class BankAccountControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/common/bank_accounts";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyBankAccount() {
    }

    @Test
    void verifyBalance() {
    }

    @Test
    void transaction() {
    }

    @Test
    void findByFilter() {
    }

    @Transactional
    @Test
    void createTest() throws Exception {
        String body = """
                {
                  "ownerUserId": 1
                }""";

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}