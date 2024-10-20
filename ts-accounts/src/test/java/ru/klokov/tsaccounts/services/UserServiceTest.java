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
import ru.klokov.tsaccounts.dtos.CreateOrUpdateUserDto;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tscommon.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tscommon.specifications.search_models.UserSearchModel;
import ru.klokov.tsaccounts.test_objects.UserSearchModelReturner;
import ru.klokov.tscommon.dtos.UserSimpleDataDto;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(TestContainerConfExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;

    private final String CREATE_USERNAME = "username";
    private final String CREATE_FIRST_NAME = "Fortest";
    private final String CREATE_SECOND_NAME = "Fortestov";
    private final String CREATE_THIRD_NAME = "Fortestovich";
    private final String CREATE_EMAIL = "test@test.ru";
    private final String CREATE_PHONE_NUMBER = "+7 (123) 456-7890";

    @Test
    @Transactional
    void createTest() {
        CreateOrUpdateUserDto userDto = new CreateOrUpdateUserDto();
        userDto.setUsername(CREATE_USERNAME);
        userDto.setFirstName(CREATE_FIRST_NAME);
        userDto.setSecondName(CREATE_SECOND_NAME);
        userDto.setThirdName(CREATE_THIRD_NAME);
        userDto.setEmail(CREATE_EMAIL);
        userDto.setPhoneNumber(CREATE_PHONE_NUMBER);

        UserModel userToCreate = userService.create(userDto);
        UserModel createdUser = userService.findById(userToCreate.getId());

        assertNotNull(createdUser);
        assertEquals(CREATE_USERNAME, createdUser.getUsername());
        assertEquals(CREATE_FIRST_NAME, createdUser.getFirstName());
        assertEquals(CREATE_SECOND_NAME, createdUser.getSecondName());
        assertEquals(CREATE_THIRD_NAME, createdUser.getThirdName());
        assertEquals(CREATE_EMAIL, createdUser.getEmail());
        assertEquals(CREATE_PHONE_NUMBER, createdUser.getPhoneNumber());
        assertFalse(createdUser.getBlocked());
        assertFalse(createdUser.getDeleted());
    }

    @Test
    @Transactional
    void createAlreadyCreatedUser() {
        CreateOrUpdateUserDto userDto = new CreateOrUpdateUserDto();
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
        CreateOrUpdateUserDto userDto = new CreateOrUpdateUserDto();
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

        Page<UserModel> result = userService.findByFilter(modelWithOneId);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        UserModel model = result.getContent().get(0);

        assertEquals(1, model.getId());
        assertEquals("testusername", model.getUsername());
        assertEquals("Testov", model.getSecondName());
        assertEquals("Test", model.getFirstName());
        assertNull(model.getThirdName());
        assertEquals("test@example.com", model.getEmail());
        assertEquals("+1 (555) 123-4567", model.getPhoneNumber());
        assertEquals(false, model.getBlocked());
        assertEquals(false, model.getDeleted());
    }

    @Test
    void findByFilterTest2() {
        UserSearchModel modelWithThreeId = UserSearchModelReturner.returnModelWithThreeId();

        Page<UserModel> result = userService.findByFilter(modelWithThreeId);
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());

        UserModel model1 = result.getContent().get(0);
        UserModel model2 = result.getContent().get(1);
        UserModel model3 = result.getContent().get(2);

        assertEquals(1, model1.getId());
        assertEquals("testusername", model1.getUsername());
        assertEquals("Testov", model1.getSecondName());
        assertEquals("Test", model1.getFirstName());
        assertNull(model1.getThirdName());
        assertEquals("test@example.com", model1.getEmail());
        assertEquals("+1 (555) 123-4567", model1.getPhoneNumber());
        assertEquals(false, model1.getBlocked());
        assertEquals(false, model1.getDeleted());

        assertEquals(3, model2.getId());
        assertEquals("blockeduser", model2.getUsername());
        assertEquals("Blockov", model2.getSecondName());
        assertEquals("Block", model2.getFirstName());
        assertEquals("Blockovic", model2.getThirdName());
        assertEquals("blocked@example.com", model2.getEmail());
        assertEquals("+2 (333) 345-6789", model2.getPhoneNumber());
        assertEquals(true, model2.getBlocked());
        assertEquals(false, model2.getDeleted());

        assertEquals(5, model3.getId());
        assertEquals("threeaccsuser", model3.getUsername());
        assertEquals("Threeaccov", model3.getSecondName());
        assertEquals("Threeacc", model3.getFirstName());
        assertEquals("Threeaccovich", model3.getThirdName());
        assertEquals("threeacc@example.com", model3.getEmail());
        assertEquals("+3 (333) 567-8901", model3.getPhoneNumber());
        assertEquals(false, model3.getBlocked());
        assertEquals(false, model3.getDeleted());
    }

    @Test
    void findByFilterTest3() {
        UserSearchModel modelWithWord = UserSearchModelReturner.returnModelWithUsernameContainsWord();

        Page<UserModel> result = userService.findByFilter(modelWithWord);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        UserModel model1 = result.getContent().get(0);
        UserModel model2 = result.getContent().get(1);

        assertEquals(5, model1.getId());
        assertEquals("threeaccsuser", model1.getUsername());
        assertEquals("Threeaccov", model1.getSecondName());
        assertEquals("Threeacc", model1.getFirstName());
        assertEquals("Threeaccovich", model1.getThirdName());
        assertEquals("threeacc@example.com", model1.getEmail());
        assertEquals("+3 (333) 567-8901", model1.getPhoneNumber());
        assertEquals(false, model1.getBlocked());
        assertEquals(false, model1.getDeleted());

        assertEquals(2, model2.getId());
        assertEquals("twoaccsuser", model2.getUsername());
        assertEquals("Twoaccov", model2.getSecondName());
        assertEquals("Twoacc", model2.getFirstName());
        assertEquals("Twoaccovich", model2.getThirdName());
        assertEquals("twoacc@example.com", model2.getEmail());
        assertEquals("+1 (222) 234-5678", model2.getPhoneNumber());
        assertEquals(false, model2.getBlocked());
        assertEquals(false, model2.getDeleted());
    }

    @Test
    void findByFilterTest4() {
        UserSearchModel modelWithTwoSearchFields = UserSearchModelReturner.returnModelWithTwoSearchFields();

        Page<UserModel> result = userService.findByFilter(modelWithTwoSearchFields);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        UserModel model = result.getContent().get(0);

        assertEquals(5, model.getId());
        assertEquals("threeaccsuser", model.getUsername());
        assertEquals("Threeaccov", model.getSecondName());
        assertEquals("Threeacc", model.getFirstName());
        assertEquals("Threeaccovich", model.getThirdName());
        assertEquals("threeacc@example.com", model.getEmail());
        assertEquals("+3 (333) 567-8901", model.getPhoneNumber());
        assertEquals(false, model.getBlocked());
        assertEquals(false, model.getDeleted());
    }

    @Test
    void findByFilterTest5() {
        UserSearchModel modelWithTwoSearchFields = new UserSearchModel();

        Page<UserModel> result = userService.findByFilter(modelWithTwoSearchFields);
        assertNotNull(result);
        assertEquals(8, result.getTotalElements());
    }

    @Test
    void findByFilterWithWrongSortFieldTest() {
        UserSearchModel model = UserSearchModelReturner.returnModelWithWrongSortField();

        assertThrows(VerificationException.class, () -> userService.findByFilter(model));
    }

    @Test
    void findUserByBankAccountIdTest() {
        UserModel result = userService.findUserByBankAccountId(5L);

        assertNotNull(result);

        assertEquals(4, result.getId());
        assertEquals("deleteduser", result.getUsername());
        assertEquals("Delov", result.getSecondName());
        assertEquals("Del", result.getFirstName());
        assertEquals("Delovich", result.getThirdName());
        assertEquals("deleted@example.com", result.getEmail());
        assertEquals("+2 (444) 456-7890", result.getPhoneNumber());
        assertEquals(false, result.getBlocked());
        assertEquals(true, result.getDeleted());

        Long nonExistingBankAccountId = 1000000000000000000L;

        assertThrows(NoMatchingEntryInDatabaseException.class, () -> userService.findUserByBankAccountId(nonExistingBankAccountId));
    }

    @Test
    void findUsersByBankAccountIdsListTest() {
        List<Long> ids = List.of(1L, 5L, 4L, 4L, 6L, 7L, 8L);

        Set<UserSimpleDataDto> result = userService.findUsersByBankAccountIdsList(ids);

        assertFalse(result.isEmpty());

        assertEquals(4, result.size());

        List<UserSimpleDataDto> resultList = new java.util.ArrayList<>(result.stream().toList());
        resultList.sort(Comparator.comparing(UserSimpleDataDto::getUserId));

        assertEquals(1, resultList.get(0).getUserId());
        assertEquals("testusername", resultList.get(0).getUsername());
        assertTrue(resultList.get(0).getBankAccountIds().containsAll(List.of(1L)) && List.of(1L).containsAll(resultList.get(0).getBankAccountIds()));

        assertEquals(3, resultList.get(1).getUserId());
        assertEquals("blockeduser", resultList.get(1).getUsername());
        assertTrue(resultList.get(1).getBankAccountIds().containsAll(List.of(4L)) && List.of(4L).containsAll(resultList.get(1).getBankAccountIds()));

        assertEquals(4, resultList.get(2).getUserId());
        assertEquals("deleteduser", resultList.get(2).getUsername());
        assertTrue(resultList.get(2).getBankAccountIds().containsAll(List.of(5L)) && List.of(5L).containsAll(resultList.get(2).getBankAccountIds()));

        assertEquals(5, resultList.get(3).getUserId());
        assertEquals("threeaccsuser", resultList.get(3).getUsername());
        assertTrue(resultList.get(3).getBankAccountIds().containsAll(List.of(6L, 7L, 8L)) && List.of(6L, 7L, 8L).containsAll(resultList.get(3).getBankAccountIds()));
    }
}
