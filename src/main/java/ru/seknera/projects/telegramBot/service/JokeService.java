package ru.seknera.projects.telegramBot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.seknera.projects.telegramBot.model.JokeModel;

public interface JokeService {

    Page<JokeModel> getAllJokeModels(Pageable pageable);

    Optional<JokeModel> getJokeModelById(Long id);

    JokeModel addJokeModel(JokeModel jokeModel);

    Optional<JokeModel> updateJokeModel(Long id, JokeModel jokeModel);

    void deleteJokeModelById(Long id);

    List<JokeModel> getTop5Jokes();

    Optional<JokeModel> getRandomJoke();

}
