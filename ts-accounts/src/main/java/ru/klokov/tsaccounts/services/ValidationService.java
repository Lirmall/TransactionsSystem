package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tsaccounts.repositories.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserRepository userRepository;

    public void validateUsername(String username) {
        Optional<UserEntity> foundUser = userRepository.findUserEntityByUsername(username);

        if(foundUser.isPresent()) {
            log.error("Error with create user - User with username \"{}\" already exists in the system", username);
            throw new AlreadyCreatedException(String.format("User with username \"%s\" already exists in the system", username));
        }
    }
}
