package ru.klokov.tsaccounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tsaccounts.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
