package ru.seknera.projects.telegramBot.repository;

import java.util.List;
import java.util.Optional;
import ru.seknera.projects.telegramBot.model.JokeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JokeRepository extends JpaRepository<JokeModel, Long> {

    List<JokeModel> getJokeModelsBy();

    Optional<JokeModel> getJokeModelById(Long id);

    void deleteById(Long id);
}
