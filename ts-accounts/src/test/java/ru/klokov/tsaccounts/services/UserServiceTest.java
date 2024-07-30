package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.config.TestContainerConfExtension;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.exceptions.VerificationException;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tsaccounts.specifications.user.UserSearchModel;
import ru.klokov.tsaccounts.test_objects.UserSearchModelReturner;
import ru.klokov.tsaccounts.test_utils.FieldsSouter;

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
        assertEquals(8, result.size());
    }

    @Test
    void findByFilterTest() {
        UserSearchModel modelWithOneId = UserSearchModelReturner.returnModelWithOneId();

        Page<UserDto> result = userService.findByFilter(modelWithOneId);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        UserDto dto = result.getContent().get(0);

        assertEquals(1, dto.getId());
        assertEquals("testusername", dto.getUsername());
        assertEquals("Testov", dto.getSecondName());
        assertEquals("Test", dto.getFirstName());
        assertNull(dto.getThirdName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("+1 (555) 123-4567", dto.getPhoneNumber());
        assertEquals(false, dto.getBlocked());
        assertEquals(false, dto.getDeleted());
    }

    @Test
    void findByFilterTest2() {
        UserSearchModel modelWithThreeId = UserSearchModelReturner.returnModelWithThreeId();

        Page<UserDto> result = userService.findByFilter(modelWithThreeId);
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());

        UserDto dto1 = result.getContent().get(0);
        UserDto dto2 = result.getContent().get(1);
        UserDto dto3 = result.getContent().get(2);

        assertEquals(1, dto1.getId());
        assertEquals("testusername", dto1.getUsername());
        assertEquals("Testov", dto1.getSecondName());
        assertEquals("Test", dto1.getFirstName());
        assertNull(dto1.getThirdName());
        assertEquals("test@example.com", dto1.getEmail());
        assertEquals("+1 (555) 123-4567", dto1.getPhoneNumber());
        assertEquals(false, dto1.getBlocked());
        assertEquals(false, dto1.getDeleted());

        assertEquals(3, dto2.getId());
        assertEquals("blockeduser", dto2.getUsername());
        assertEquals("Blockov", dto2.getSecondName());
        assertEquals("Block", dto2.getFirstName());
        assertEquals("Blockovic", dto2.getThirdName());
        assertEquals("blocked@example.com", dto2.getEmail());
        assertEquals("+2 (333) 345-6789", dto2.getPhoneNumber());
        assertEquals(true, dto2.getBlocked());
        assertEquals(false, dto2.getDeleted());

        assertEquals(5, dto3.getId());
        assertEquals("threeaccsuser", dto3.getUsername());
        assertEquals("Threeaccov", dto3.getSecondName());
        assertEquals("Threeacc", dto3.getFirstName());
        assertEquals("Threeaccovich", dto3.getThirdName());
        assertEquals("threeacc@example.com", dto3.getEmail());
        assertEquals("+3 (333) 567-8901", dto3.getPhoneNumber());
        assertEquals(false, dto3.getBlocked());
        assertEquals(false, dto3.getDeleted());
    }

    @Test
    void findByFilterTest3() {
        UserSearchModel modelWithWord = UserSearchModelReturner.returnModelWithUsernameContainsWord();

        Page<UserDto> result = userService.findByFilter(modelWithWord);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        UserDto dto1 = result.getContent().get(0);
        UserDto dto2 = result.getContent().get(1);

        assertEquals(5, dto1.getId());
        assertEquals("threeaccsuser", dto1.getUsername());
        assertEquals("Threeaccov", dto1.getSecondName());
        assertEquals("Threeacc", dto1.getFirstName());
        assertEquals("Threeaccovich", dto1.getThirdName());
        assertEquals("threeacc@example.com", dto1.getEmail());
        assertEquals("+3 (333) 567-8901", dto1.getPhoneNumber());
        assertEquals(false, dto1.getBlocked());
        assertEquals(false, dto1.getDeleted());

        assertEquals(2, dto2.getId());
        assertEquals("twoaccsuser", dto2.getUsername());
        assertEquals("Twoaccov", dto2.getSecondName());
        assertEquals("Twoacc", dto2.getFirstName());
        assertEquals("Twoaccovich", dto2.getThirdName());
        assertEquals("twoacc@example.com", dto2.getEmail());
        assertEquals("+1 (222) 234-5678", dto2.getPhoneNumber());
        assertEquals(false, dto2.getBlocked());
        assertEquals(false, dto2.getDeleted());
    }

    @Test
    void findByFilterTest4() {
        UserSearchModel modelWithTwoSearchFields = UserSearchModelReturner.returnModelWithTwoSearchFields();

        Page<UserDto> result = userService.findByFilter(modelWithTwoSearchFields);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        UserDto dto = result.getContent().get(0);

        assertEquals(5, dto.getId());
        assertEquals("threeaccsuser", dto.getUsername());
        assertEquals("Threeaccov", dto.getSecondName());
        assertEquals("Threeacc", dto.getFirstName());
        assertEquals("Threeaccovich", dto.getThirdName());
        assertEquals("threeacc@example.com", dto.getEmail());
        assertEquals("+3 (333) 567-8901", dto.getPhoneNumber());
        assertEquals(false, dto.getBlocked());
        assertEquals(false, dto.getDeleted());
    }

    @Test
    void findByFilterWithWrongSortFieldTest() {
        UserSearchModel model = UserSearchModelReturner.returnModelWithWrongSortField();

        assertThrows(VerificationException.class, () -> userService.findByFilter(model));
    }
}