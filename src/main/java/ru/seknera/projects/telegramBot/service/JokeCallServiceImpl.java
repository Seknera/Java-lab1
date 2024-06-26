package ru.seknera.projects.telegramBot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.model.JokeCallModel;
import ru.seknera.projects.telegramBot.repository.JokeCallRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JokeCallServiceImpl implements JokeCallService{

    private final JokeCallRepository jokeCallRepository;

    @Override
    public List<JokeCallModel> getAllJokeCall() {
        return jokeCallRepository.findAll();
    }

    @Override
    public JokeCallModel addJokeCall(JokeCallModel jokeCallModel) {
        return jokeCallRepository.save(jokeCallModel);
    }
}
