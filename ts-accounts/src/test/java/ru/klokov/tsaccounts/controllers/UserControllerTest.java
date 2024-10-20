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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        String body = """
                {
                  "username": "testfromcontroller",
                  "secondName": "Fromcontrollerov",
                  "firstName": "Fromcontriller",
                  "thirdName": "Fromcontrollerovich",
                  "email": "test@controller.ru",
                  "phoneNumber": "+1 (888) 765-2528"
                }""";

            mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void createThrowExceptionTest() throws Exception {
        String body = """
                {
                  "username": "testusername",
                  "secondName": "Fromcontrollerov",
                  "firstName": "Fromcontriller",
                  "thirdName": "Fromcontrollerovich",
                  "email": "test@controller.ru",
                  "phoneNumber": "+1 (888) 765-2528"
                }""";

            mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().is(409));
    }

    @Transactional
    @Test
    void updateTest() throws Exception {
        String body = """
                {
                  "username": "updatedfromcontrolleruser",
                  "secondName": "Updatefromcontrollerov",
                  "firstName": "Updatefromcontroller",
                  "thirdName": "Updatefromcontrollerovich",
                  "email": "test@controller.ru",
                  "phoneNumber": "+1 (888) 765-2528"
                }""";

            mockMvc.perform(post(URL_TEMPLATE + "/update")
                            .param("id", "8")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andDo(print())
            ;
    }

    @Test
    void findByIdTest() throws Exception {
            mockMvc.perform(get(URL_TEMPLATE + "/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.username").value("testusername"))
                    .andExpect(jsonPath("$.secondName").value("Testov"))
                    .andExpect(jsonPath("$.firstName").value("Test"))
                    .andExpect(jsonPath("$.thirdName").doesNotExist())
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.phoneNumber").value("+1 (555) 123-4567"))
                    .andExpect(jsonPath("$.blocked").value(false))
                    .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void findByFilterTest() throws Exception {
        String body = """
                {
                  "ids": [1, 2, 3, 4, 5, 6],
                  "sortColumn": "-id",
                  "pages": 1
                }""";

            mockMvc.perform(post(URL_TEMPLATE + "/filter")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.content.[0].id").value(1))
                    .andExpect(jsonPath("$.content.[0].username").value("testusername"))
                    .andExpect(jsonPath("$.content.[0].secondName").value("Testov"))
                    .andExpect(jsonPath("$.content.[0].firstName").value("Test"))
                    .andExpect(jsonPath("$.content.[0].thirdName").doesNotExist())
                    .andExpect(jsonPath("$.content.[0].email").value("test@example.com"))
                    .andExpect(jsonPath("$.content.[0].phoneNumber").value("+1 (555) 123-4567"))
                    .andExpect(jsonPath("$.content.[0].blocked").value(false))
                    .andExpect(jsonPath("$.content.[0].deleted").value(false));
    }
}
