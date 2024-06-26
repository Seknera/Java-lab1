package ru.seknera.projects.telegramBot.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.seknera.projects.telegramBot.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}