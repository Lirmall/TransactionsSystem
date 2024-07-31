package ru.klokov.tsaccounts.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
class UserControllerTest {
    private static final String URL_TEMPLATE = "/api/v1/common/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllTest() throws Exception {
            mockMvc.perform(get(URL_TEMPLATE))
                    .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void createTest() throws Exception {
        String body = "{\n" +
                "  \"username\": \"testfromcontroller\",\n" +
                "  \"secondName\": \"Fromcontrollerov\",\n" +
                "  \"firstName\": \"Fromcontriller\",\n" +
                "  \"thirdName\": \"Fromcontrollerovich\",\n" +
                "  \"email\": \"test@controller.ru\",\n" +
                "  \"phoneNumber\": \"+1(888) 765-2528\"\n" +
                "}";

            mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());
    }
}