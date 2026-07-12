package com.example.mary.cinema.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для ответа клиенту с информацией о киносеансе.
 * Содержит movieId (идентификатор связанного фильма) вместо полной сущности Movie.
 */
@Data
public class SessionResponseDTO {
    private Long id;
    private Long movieId;
    private LocalDateTime startTime;
    private int hallNumber;
    private int totalSeats;
}