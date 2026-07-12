package com.example.mary.cinema.dto;

import lombok.Data;

/**
 * DTO для ответа клиенту с информацией о забронированном билете.
 */
@Data
public class TicketResponseDTO {
    private Long id;
    private Long sessionId;
    private String customerName;
    private int seatNumber;
}