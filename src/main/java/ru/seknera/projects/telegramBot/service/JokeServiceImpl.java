package ru.seknera.projects.telegramBot.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.model.JokeModel;
import ru.seknera.projects.telegramBot.repository.JokeRepository;

@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;

    @Override
    public List<JokeModel> getAllJokeModels() {
        return jokeRepository.getJokeModelsBy();
    }

    @Override
    public Optional<JokeModel> getJokeModelById(Long id) {
        return jokeRepository.findById(id);
    }

    @Override
    public JokeModel addJokeModel(JokeModel jokeModel) {
        jokeModel.setDateOfCreation(LocalDateTime.now());
        jokeModel.setDateOfUpdate(null);
        return jokeRepository.save(jokeModel);
    }

    @Override
    public Optional<JokeModel> updateJokeModel(Long id, JokeModel jokeModel) {
        Optional<JokeModel> jokeModelOptional = jokeRepository.findById(id);
        if (jokeModelOptional.isPresent()) {
            jokeModel.setTheTextOfTheJoke(jokeModel.getTheTextOfTheJoke());
            jokeModel.setDateOfUpdate(LocalDateTime.now());
        }
        else {
            jokeModel.setId(null);
        }
        return Optional.of(jokeRepository.save(jokeModel));
    }

    @Override
    public void deleteJokeModelById(Long id) {
        jokeRepository.deleteById(id);
    }
}
