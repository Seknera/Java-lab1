package ru.seknera.projects.telegramBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.exceptions.UsernameAlreadyExistsException;
import ru.seknera.projects.telegramBot.model.JokeModel;
import ru.seknera.projects.telegramBot.model.JokeCallModel;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotService {

    private final TelegramBot telegramBot;
    private final JokeService jokeService;
    private final JokeCallService jokeCallService;
    private final UserService userService;

    @Autowired
    public TelegramBotService(TelegramBot telegramBot, JokeService jokeService, JokeCallService jokeCallService, UserService userService, PasswordEncoder passwordEncoder) {
        this.telegramBot = telegramBot;
        this.jokeService = jokeService;
        this.jokeCallService = jokeCallService;
        this.userService = userService;
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void processUpdate(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();
        Long userId = update.message().from().id().longValue();

        if (messageText.startsWith("/getjokes")) {
            getAllJokes(chatId);
        }
        else if (messageText.startsWith("/getjoke ")) {
            getJokeById(chatId, messageText.replace("/getjoke ", ""), userId);
        }
        else if (messageText.startsWith("/addjoke ")) {
            addJoke(chatId, messageText.replace("/addjoke ", ""));
        }
        else if (messageText.startsWith("/updatejoke ")) {
            updateJoke(chatId, messageText.replace("/updatejoke", ""));
        }
        else if (messageText.startsWith("/deletejoke ")) {
            deleteJoke(chatId, messageText.replace("/deletejoke", ""));
        }
        else if (messageText.startsWith("/randomjoke")) {
            randomJoke(chatId, userId);
        }
        else if (messageText.startsWith("/top5")) {
            top5Jokes(chatId);
        }
        else if (messageText.startsWith("/registration ")) {
            registerUser(chatId, messageText.replace("/registration", ""));
        }
        else
            unknownCommand(chatId);
    }

    private void top5Jokes(Long chatId) {
        List<JokeModel> top5Jokes = jokeService.getTop5Jokes();
        if (!top5Jokes.isEmpty()) {
            StringBuilder response = new StringBuilder("Топ 5 самых популярных шуток:\n");
            for (int i = 0; i < top5Jokes.size(); i++) {
                response.append(i + 1).append(": ").append(top5Jokes.get(i).getTheTextOfTheJoke()).append("\n");
            }
            sendMessage(chatId, response.toString());
        } else {
            sendMessage(chatId, "Шутки не найдены.");
        }
    }

    private void randomJoke(Long chatId, Long userId) {
        Optional<JokeModel> joke = jokeService.getRandomJoke();
        if (joke.isPresent()) {
            sendMessage(chatId, joke.get().getTheTextOfTheJoke());
            JokeCallModel jokeCall = new JokeCallModel();
            jokeCall.setJokeId(joke.get().getId());
            jokeCall.setUserId(userId);
            jokeCallService.addJokeCall(jokeCall);
        } else {
            sendMessage(chatId, "Шутка не найдена.");
        }
    }
    private void getAllJokes(Long chatId) {
        int page = 0;
        int size = 5;

        Pageable pageable = PageRequest.of(page, size);
        Page<JokeModel> jokesPage = jokeService.getAllJokeModels(pageable);

        StringBuilder response = new StringBuilder("Все шутки (страница ").append(jokesPage.getNumber() + 1)
                .append(" из ").append(jokesPage.getTotalPages()).append("):\n");

        for (JokeModel joke : jokesPage.getContent()) {
            response.append(joke.getId()).append(": ").append(joke.getTheTextOfTheJoke()).append("\n");
        }

        sendMessage(chatId, response.toString());
    }

    private void getJokeById(Long chatId, String idText, Long userId) {
        try {
            Long id = Long.parseLong(idText);
            Optional<JokeModel> joke = jokeService.getJokeModelById(id);
            if (joke.isPresent()) {
                sendMessage(chatId, joke.get().getTheTextOfTheJoke());
                JokeCallModel jokeCall = new JokeCallModel();
                jokeCall.setJokeId(joke.get().getId());
                jokeCall.setUserId(userId);
                jokeCallService.addJokeCall(jokeCall);
            } else {
                sendMessage(chatId, "Шутка не найдена.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void addJoke(Long chatId, String jokeText) {
        JokeModel newJoke = new JokeModel();
        newJoke.setTheTextOfTheJoke(jokeText);
        JokeModel savedJoke = jokeService.addJokeModel(newJoke);
        sendMessage(chatId, "Шутка добавлена с ID: " + savedJoke.getId());
    }

    private void updateJoke(Long chatId, String text) {
        String[] parts = text.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(chatId, "Использование: /updatejoke <id> <новый текст шутки>");
            return;
        }
        try {
            Long id = Long.parseLong(parts[0]);
            String jokeText = parts[1];
            JokeModel jokeModel = new JokeModel();
            jokeModel.setTheTextOfTheJoke(jokeText);
            Optional<JokeModel> updatedJoke = jokeService.updateJokeModel(id, jokeModel);
            if (updatedJoke.isPresent()) {
                sendMessage(chatId, "Шутка обновлена.");
            } else {
                sendMessage(chatId, "Шутки с таким ID не существует.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void deleteJoke(Long chatId, String idText) {
        try {
            Long id = Long.parseLong(idText);
            jokeService.deleteJokeModelById(id);
            sendMessage(chatId, "Шутка удалена.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void registerUser(Long chatId, String text) {
        String[] parts = text.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(chatId, "Использование: /registration <username> <password>");
            return;
        }
        String username = parts[0];
        String password = parts[1];
        try {
            userService.registration(username, password);
            sendMessage(chatId, "Регистрация успешна.");
        } catch (UsernameAlreadyExistsException e) {
            sendMessage(chatId, "Такое имя уже используется.");
        }
    }

    private void unknownCommand(Long chatId) {
        sendMessage(chatId, "Неизвестная команда.");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        telegramBot.execute(request);
    }
}

