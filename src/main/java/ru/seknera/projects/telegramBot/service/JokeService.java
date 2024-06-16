package ru.seknera.projects.telegramBot.service;

import java.util.List;
import java.util.Optional;
import ru.seknera.projects.telegramBot.model.JokeModel;

public interface JokeService {

    List<JokeModel> getAllJokeModels();

    Optional<JokeModel> getJokeModelById(Long id);

    JokeModel addJokeModel(JokeModel jokeModel);

    Optional<JokeModel> updateJokeModel(Long id, JokeModel jokeModel);

    void deleteJokeModelById(Long id);

}
