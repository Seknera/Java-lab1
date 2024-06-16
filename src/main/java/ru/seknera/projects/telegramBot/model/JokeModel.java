package ru.seknera.projects.telegramBot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.*;

@AllArgsConstructor //Генерируем конструктор с параметрами
@NoArgsConstructor //Генерируем конструктор без параметров
@Getter //Генерируем геттеры
@Setter //Генерируем сеттеры
@ToString //Отдельный метод для toString
@Entity(name = "jokes") //Объявляем класс как сущность для работы с ним в БД и его имя
@Table(name = "jokes") //Помечаем, как называется таблица в БД
@EqualsAndHashCode
public class JokeModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "the_text_of_the_joke")
    private String theTextOfTheJoke;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation = LocalDateTime.now();

    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate = null;

    @PreUpdate
    protected void onUpdate() {
        this.dateOfUpdate = LocalDateTime.now();
    }

}

