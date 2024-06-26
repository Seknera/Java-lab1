package ru.seknera.projects.telegramBot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.seknera.projects.telegramBot.model.UserRole;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
}