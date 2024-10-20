package ru.klokov.tstransactions.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tstransactions.config.TestContainerConfExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class TransactionControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/common/transactions";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByFilter() throws Exception {
        String body = """
                {
                  "pages": 0,
                  "limit": 5,
                  "sortColumn": "transactionDate",
                  "criteriaList": [
                    {
                      "fieldName": "transactionDate",
                      "searchOperation": ">",
                      "fieldValue": "1970-01-01T00:00:00.000000001",
                      "orPredicate": false
                    }
                  ]
                }""";

        mockMvc.perform(post(URL_TEMPLATE + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void findByPeriod() {
    }
}
