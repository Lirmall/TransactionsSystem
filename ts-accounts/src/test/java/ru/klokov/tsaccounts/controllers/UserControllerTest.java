package ru.klokov.tsaccounts.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class UserControllerTest {
    private static String URL_TEMPLATE = "/api/v1/common/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
            mockMvc.perform(get(URL_TEMPLATE))
                    .andExpect(status().isOk());
    }
}