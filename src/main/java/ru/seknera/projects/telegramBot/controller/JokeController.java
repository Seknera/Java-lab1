package ru.seknera.projects.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.seknera.projects.telegramBot.service.JokeService;
import ru.seknera.projects.telegramBot.model.JokeModel;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jokes")
public class JokeController {

    JokeService jokeService;

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    // GET /jokes
    @GetMapping
    ResponseEntity<Page<JokeModel>> getAllJokeModels(Pageable pageable) {
        return ResponseEntity.ok(jokeService.getAllJokeModels(pageable));
    }

    // GET /jokes/id
    @GetMapping("/{id}")
    ResponseEntity<JokeModel> getJokeModelById(@PathVariable("id") Long id) {
        return jokeService.getJokeModelById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // POST /jokes
    @PostMapping
    ResponseEntity<JokeModel> addJokeModel(@RequestBody JokeModel jokeModel) {
        return ResponseEntity.ok(jokeService.addJokeModel(jokeModel));
    }

    // PUT /jokes/id
    @PutMapping("/{id}")
    ResponseEntity<JokeModel> updateJokeModel(@PathVariable("id") Long id, @RequestBody JokeModel jokeModel) {
        Optional<JokeModel> updatedJoke = jokeService.updateJokeModel(id, jokeModel);
        return updatedJoke.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    // DELETE /jokes/id
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteJokeModel(@PathVariable("id") Long id) {
        jokeService.deleteJokeModelById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /jokes/top5
    @GetMapping("/top5")
    ResponseEntity<List<JokeModel>> getTop5Jokes() {
        return ResponseEntity.ok(jokeService.getTop5Jokes());
    }

    // GET /jokes/random
    @GetMapping("/random")
    public ResponseEntity<JokeModel> getRandomJoke() {
        Optional<JokeModel> joke = jokeService.getRandomJoke();
        return joke.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
