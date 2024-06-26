package ru.seknera.projects.telegramBot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "joke_calls")
@Table(name = "joke_calls")
public class JokeCallModel {

    @Id
    @Column(name = "id_call")
    @GeneratedValue(generator = "call_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "call_id_seq", sequenceName = "call_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "id_joke")
    private Long jokeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "call_time")
    private LocalDateTime callTime = LocalDateTime.now();
}
