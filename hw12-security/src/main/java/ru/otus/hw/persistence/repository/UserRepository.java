package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.persistence.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String userName);

}
