package ru.seknera.projects.telegramBot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.seknera.projects.telegramBot.model.JokeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.seknera.projects.telegramBot.model.JokeCallModel;

public interface JokeRepository extends JpaRepository<JokeModel, Long> {

    Page<JokeModel> findAll(Pageable pageable);

    Optional<JokeModel> getJokeModelById(Long id);

    void deleteById(Long id);

    @Query(value = "SELECT j FROM jokes j ORDER BY (SELECT COUNT(c) FROM joke_calls c WHERE c.jokeId = j.id) DESC")
    Page<JokeModel> findTop5Jokes(Pageable pageable);

    @Query(value = "SELECT * FROM jokes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    JokeModel findRandomJoke();
}

