package ru.seknera.projects.telegramBot.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.model.JokeCallModel;
import java.util.List;


public interface JokeCallService {
    List<JokeCallModel> getAllJokeCall();
    JokeCallModel addJokeCall(JokeCallModel jokeCallModel);
}
