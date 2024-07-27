package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tsaccounts.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByUsername(String username);
}
