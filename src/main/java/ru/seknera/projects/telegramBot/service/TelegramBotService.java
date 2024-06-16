package ru.seknera.projects.telegramBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.model.JokeModel;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotService {

    private final TelegramBot telegramBot;
    private final JokeService jokeService;

    @Autowired
    public TelegramBotService(TelegramBot telegramBot, JokeService jokeService) {
        this.telegramBot = telegramBot;
        this.jokeService = jokeService;
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void processUpdate(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        if (!messageText.startsWith("/") || messageText.length() == 1) {
            handleUnknownCommand(chatId);
            return;
        }

        if (messageText.startsWith("/getjokes")) {
            handleGetAllJokes(chatId);
        }
        else if (messageText.startsWith("/getjoke")) {
            handleGetJokeById(chatId, messageText.replace("/getjoke", ""));
        }
        else if (messageText.startsWith("/addjoke")) {
            handleAddJoke(chatId, messageText.replace("/addjoke ", ""));
        }
        else if (messageText.startsWith("/updatejoke")) {
            handleUpdateJoke(chatId, messageText.replace("/updatejoke", ""));
        }
        else if (messageText.startsWith("/deletejoke")) {
            handleDeleteJoke(chatId, messageText.replace("/deletejoke", ""));
        }
    }

    private void handleGetAllJokes(Long chatId) {
        List<JokeModel> jokes = jokeService.getAllJokeModels();
        StringBuilder response = new StringBuilder("Вот все шутки:\n");
        for (JokeModel joke : jokes) {
            response.append(joke.getId()).append(": ").append(joke.getTheTextOfTheJoke()).append("\n");
        }
        sendMessage(chatId, response.toString());
    }

    private void handleGetJokeById(Long chatId, String idText) {
        try {
            Long id = Long.parseLong(idText);
            Optional<JokeModel> joke = jokeService.getJokeModelById(id);
            if (joke.isPresent()) {
                sendMessage(chatId, joke.get().getTheTextOfTheJoke());
            } else {
                sendMessage(chatId, "Шутка не найдена.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleAddJoke(Long chatId, String jokeText) {
        JokeModel newJoke = new JokeModel();
        newJoke.setTheTextOfTheJoke(jokeText);
        JokeModel savedJoke = jokeService.addJokeModel(newJoke);
        sendMessage(chatId, "Шутка добавлена с ID: " + savedJoke.getId());
    }

    private void handleUpdateJoke(Long chatId, String text) {
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
                sendMessage(chatId, "Шутка не найдена.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleDeleteJoke(Long chatId, String idText) {
        try {
            Long id = Long.parseLong(idText);
            jokeService.deleteJokeModelById(id);
            sendMessage(chatId, "Шутка удалена.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleUnknownCommand(Long chatId) {
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

