package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.NoMatchingEntryInDatabaseException;
import ru.klokov.tsaccounts.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity create(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public UserEntity findById(Long id) {
        Optional<UserEntity> foundUser = userRepository.findById(id);

        if(foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new NoMatchingEntryInDatabaseException(String.format("User wit id %s not found in database", id));
        }
    }
}
