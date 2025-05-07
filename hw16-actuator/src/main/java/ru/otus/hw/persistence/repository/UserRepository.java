package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.persistence.model.UserEntity;

import java.util.Optional;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(UserEntity.USER_ROLES)
    Optional<UserEntity> findByUsername(String userName);

}
