package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.mappers.UserEntityMapper;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tsaccounts.repositories.UserRepository;
import ru.klokov.tsaccounts.specifications.sort.UserSortChecker;
import ru.klokov.tsaccounts.specifications.user.UserSearchModel;
import ru.klokov.tsaccounts.specifications.user.UserSpecification;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityMapper userEntityMapper;
    private final UserRepository userRepository;
    private final UserBankAccountService userBankAccountService;
    private final UserSortChecker userSortChecker;
    private final ValidationService validationService;
    private final VerificationService verificationService;

    @Transactional
    public UserModel create(UserDto userDto) {
        this.checkUserData(userDto);

        log.debug("Verification success. Create user");

        UserEntity userToSave = userEntityMapper.convertDtoToEntity(userDto);

        UserEntity userEntity = userRepository.save(userToSave);

        log.debug("User with username \"{}\" successful created", userEntity.getUsername());
        return userEntityMapper.convertEntityToModel(userEntity);
    }

    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(userEntityMapper::convertEntityToModel).toList();
    }

    @Transactional(readOnly = true)
    public UserModel findById(Long id) {
        return this.privateFindById(id);
    }

    private UserModel privateFindById(Long id) {
        Optional<UserEntity> foundUser = userRepository.findById(id);

        if(foundUser.isPresent()) {
            log.info("User successful found");
            return userEntityMapper.convertEntityToModel(foundUser.get());
        } else {
            log.error("User wit id {} not found in database", id);
            throw new NoMatchingEntryInDatabaseException(String.format("User wit id %s not found in database", id));
        }
    }

    @Transactional(readOnly = true)
    public Page<UserModel> findByFilter(UserSearchModel model) {
        Pageable pageable = userSortChecker.getPageableAndSort(model);
        Page<UserEntity> userEntities = userRepository.findAll(new UserSpecification(model), pageable);
        return userEntities.map(userEntityMapper::convertEntityToModel);
    }

    @Transactional(readOnly = true)
    public UserModel findUserByBankAccountId(Long bankAccountId) {
        return userEntityMapper.convertEntityToModel(userBankAccountService.findUserByBankAccountId(bankAccountId));
    }

    @Transactional
    public UserModel updateUserById(Long id, UserDto newUserInfo) {
        verificationService.verifyUserEmail(newUserInfo.getEmail());
        verificationService.verifyPhoneNumber(newUserInfo.getPhoneNumber());
        verificationService.verifyFirstName(newUserInfo.getFirstName());
        verificationService.verifySecondName(newUserInfo.getSecondName());
        if(newUserInfo.getThirdName() != null) {
            verificationService.verifyThirdName(newUserInfo.getThirdName());
        }

        log.debug("Try to find user with id {} in DB to update", id);

        UserEntity userToUpdate = userEntityMapper.convertModelToEntity(privateFindById(id));

        String newUsername = newUserInfo.getUsername();

        if(!newUsername.equals(userToUpdate.getUsername())) {
            validationService.validateUsername(newUsername);
            verificationService.verifyUsername(newUsername);
            userToUpdate.setUsername(newUsername);
        }

        userToUpdate.setFirstName(newUserInfo.getFirstName());
        userToUpdate.setSecondName(newUserInfo.getSecondName());
        userToUpdate.setThirdName(newUserInfo.getThirdName());
        userToUpdate.setEmail(newUserInfo.getEmail());
        userToUpdate.setPhoneNumber(newUserInfo.getPhoneNumber());

        log.debug("User with id {} successful updated", id);
        return userEntityMapper.convertEntityToModel(userRepository.save(userToUpdate));
    }

    @Transactional
    public UserDto blockUserById(Long id) {
        log.debug("Try to find user with id {} in DB", id);

        UserEntity userToBlock = userEntityMapper.convertModelToEntity(privateFindById(id));

        userToBlock.setBlocked(true);
        userBankAccountService.blockBankAccountsByOwnerUserId(id);

        return userEntityMapper.convertEntityToDTO(userRepository.save(userToBlock));
    }

    private void checkUserData(UserDto dto) {
        log.debug("Check user data");
        validationService.validateUsername(dto.getUsername());
        verificationService.verifyUserEmail(dto.getEmail());
        verificationService.verifyUsername(dto.getUsername());
        verificationService.verifyPhoneNumber(dto.getPhoneNumber());
        verificationService.verifyFirstName(dto.getFirstName());
        verificationService.verifySecondName(dto.getSecondName());
        if(dto.getThirdName() != null) {
            verificationService.verifyThirdName(dto.getThirdName());
        }
        log.debug("User data is correct");
    }
}
