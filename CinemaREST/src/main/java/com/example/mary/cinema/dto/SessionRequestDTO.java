package com.example.mary.cinema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для создания нового киносеанса.
 * Содержит валидацию: movieId обязателен, hallNumber от 1, totalSeats от 1 до 300.
 */
@Data
public class SessionRequestDTO {

    /** Идентификатор фильма. Фильм должен существовать в базе. */
    @NotNull(message = "Идентификатор фильма обязателен")
    private Long movieId;

    /** Время начала сеанса. */
    @NotNull(message = "Время начала сеанса обязательно")
    private LocalDateTime startTime;

    /** Номер зала. Должен быть больше 0. */
    @Min(value = 1, message = "Номер зала должен быть больше 0")
    private int hallNumber;

    /** Общее количество мест в зале. От 1 до 300. */
    @Min(value = 1, message = "Количество мест должно быть больше 0")
    @Max(value = 300, message = "Количество мест не может превышать 300")
    private int totalSeats;
}