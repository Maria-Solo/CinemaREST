package com.example.mary.cinema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO для создания или обновления фильма.
 * Содержит валидацию входных данных согласно ТЗ.
 */
@Data
public class MovieRequestDTO {

    /** Название фильма. Не может быть пустым. */
    @NotBlank(message = "Название фильма не может быть пустым")
    private String title;

    /** Жанр фильма. Не может быть пустым. */
    @NotBlank(message = "Жанр не может быть пустым")
    private String genre;

    /** Длительность фильма в минутах. Должна быть от 1 до 600. */
    @Min(value = 1, message = "Длительность фильма должна быть больше 0 минут")
    @Max(value = 600, message = "Длительность фильма не может превышать 600 минут")
    private int durationMinutes;
}