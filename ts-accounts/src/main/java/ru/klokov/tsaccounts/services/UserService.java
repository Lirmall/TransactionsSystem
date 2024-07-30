package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.dtos.UserDto;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.exceptions.VerificationException;
import ru.klokov.tsaccounts.mappers.UserEntityMapper;
import ru.klokov.tsaccounts.models.UserModel;
import ru.klokov.tsaccounts.repositories.UserRepository;
import ru.klokov.tsaccounts.specifications.sort.UserSortChecker;
import ru.klokov.tsaccounts.specifications.user.UserSearchModel;
import ru.klokov.tsaccounts.specifications.user.UserSpecification;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityMapper userEntityMapper;
    private final UserRepository userRepository;
    private final UserSortChecker userSortChecker;

    @Transactional
    public UserModel create(UserDto entity) {
        Optional<UserEntity> foundUser = userRepository.findUserEntityByUsername(entity.getUsername());

        if(foundUser.isPresent()) {
            log.error("Error with create user - User with username \"{}\" already exists in the system", entity.getUsername());
            throw new AlreadyCreatedException(String.format("User with username \"%s\" already exists in the system", entity.getUsername()));
        }

        if(!this.emailVerification(entity.getEmail())) {
            log.error("Email verification error - \"{}\" - email does not meet requirements", entity.getEmail());
            throw new VerificationException(String.format("Email verification error - \"%s\" - email does not meet requirements", entity.getEmail()));
        }

        UserEntity userToSave = new UserEntity();
        userToSave.setUsername(entity.getUsername());
        userToSave.setFirstName(entity.getFirstName());
        userToSave.setSecondName(entity.getSecondName());
        userToSave.setThirdName(entity.getThirdName());
        userToSave.setEmail(entity.getEmail());
        userToSave.setPhoneNumber(entity.getPhoneNumber());

        log.info("User successful created");
        return userEntityMapper.convertEntityToModel(userRepository.save(userToSave));
    }

    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(userEntityMapper::convertEntityToModel).toList();
    }

    @Transactional(readOnly = true)
    public UserModel findById(Long id) {
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
    public Page<UserDto> findByFilter(UserSearchModel model) {
        Pageable pageable = userSortChecker.getPageableAndSort(model);
        Page<UserEntity> userEntities = userRepository.findAll(new UserSpecification(model), pageable);
        return userEntities.map(userEntityMapper::convertEntityToDTO);
    }

    private boolean emailVerification(String email) {
        String regex = "^[a-zA-Z0-9_\\-\\.]*@[a-zA-Z_\\-]*\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
