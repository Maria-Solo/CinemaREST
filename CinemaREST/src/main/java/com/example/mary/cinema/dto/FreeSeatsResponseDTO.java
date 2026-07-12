package com.example.mary.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для ответа на запрос свободных мест сеанса.
 * Содержит идентификатор сеанса, общее количество мест,
 * количество занятых и количество свободных мест.
 */
@Data
@AllArgsConstructor
public class FreeSeatsResponseDTO {
    private Long sessionId;
    private int totalSeats;
    private int occupiedSeats;
    private int freeSeats;
}