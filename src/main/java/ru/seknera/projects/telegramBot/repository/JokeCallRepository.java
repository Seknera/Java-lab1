package ru.seknera.projects.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.seknera.projects.telegramBot.model.JokeCallModel;

import java.util.List;

public interface JokeCallRepository extends JpaRepository<JokeCallModel, Long> {
}

