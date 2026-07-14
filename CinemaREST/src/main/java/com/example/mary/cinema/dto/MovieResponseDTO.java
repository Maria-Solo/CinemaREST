package com.example.mary.cinema.dto;

import lombok.Data;

/**
 * DTO для ответа клиенту с информацией о фильме.
 * Используется во всех endpoint-ах MovieController для отдачи данных.
 */
@Data
public class MovieResponseDTO {
    private Long id;
    private String title;
    private String genre;
    private int durationMinutes;
}