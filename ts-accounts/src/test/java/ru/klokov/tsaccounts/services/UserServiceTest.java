package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.exceptions.VerificationException;
import ru.klokov.tsaccounts.models.UserModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;

    private static final String CREATE_USERNAME = "test";
    private static final String CREATE_FIRST_NAME = "Fortest";
    private static final String CREATE_SECOND_NAME = "Fortestov";
    private static final String CREATE_THIRD_NAME = "Fortestovich";
    private static final String CREATE_EMAIL = "test@test.ru";
    private static final String CREATE_PHONE_NUMBER = "+7(123)456789";

    @Test
    @Transactional
    void createTest() {
        UserDto userDto = new UserDto();
        userDto.setUsername(CREATE_USERNAME);
        userDto.setFirstName(CREATE_FIRST_NAME);
        userDto.setSecondName(CREATE_SECOND_NAME);
        userDto.setThirdName(CREATE_THIRD_NAME);
        userDto.setEmail(CREATE_EMAIL);
        userDto.setPhoneNumber(CREATE_PHONE_NUMBER);

        UserModel createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertEquals(CREATE_USERNAME, createdUser.getUsername());
        assertEquals(CREATE_FIRST_NAME, createdUser.getFirstName());
        assertEquals(CREATE_SECOND_NAME, createdUser.getSecondName());
        assertEquals(CREATE_THIRD_NAME, createdUser.getThirdName());
        assertEquals(CREATE_EMAIL, createdUser.getEmail());
        assertEquals(CREATE_PHONE_NUMBER, createdUser.getPhoneNumber());
    }

    @Test
    @Transactional
    void createAlreadyCreatedUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testusername");
        userDto.setFirstName(CREATE_FIRST_NAME);
        userDto.setSecondName(CREATE_SECOND_NAME);
        userDto.setThirdName(CREATE_THIRD_NAME);
        userDto.setEmail(CREATE_EMAIL);
        userDto.setPhoneNumber(CREATE_PHONE_NUMBER);

        assertThrows(AlreadyCreatedException.class, () -> userService.create(userDto));
    }

    @Test
    @Transactional
    void createWrongEmail() {
        UserDto userDto = new UserDto();
        userDto.setUsername(CREATE_USERNAME);
        userDto.setFirstName(CREATE_FIRST_NAME);
        userDto.setSecondName(CREATE_SECOND_NAME);
        userDto.setThirdName(CREATE_THIRD_NAME);
        userDto.setEmail("email");
        userDto.setPhoneNumber(CREATE_PHONE_NUMBER);

        assertThrows(VerificationException.class, () -> userService.create(userDto));
    }

    @Test
    void findByIdTest() {
        Long nonExistingUserId = 10000L;
        assertDoesNotThrow(() -> userService.findById(1L));
        assertThrows(NoMatchingEntryInDatabaseException.class, () -> userService.findById(nonExistingUserId));
    }

    @Test
    void findAllTest() {
        List<UserModel> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}