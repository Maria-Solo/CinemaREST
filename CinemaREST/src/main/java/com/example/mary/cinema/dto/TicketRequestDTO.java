package com.example.mary.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для бронирования билета.
 * Содержит валидацию: customerName не пустое, seatNumber больше 0.
 */
@Data
public class TicketRequestDTO {

    /** Идентификатор сеанса, на который бронируется билет. */
    @NotNull(message = "Идентификатор сеанса обязателен")
    private Long sessionId;

    /** Имя покупателя. Не может быть пустым. */
    @NotBlank(message = "Имя покупателя не может быть пустым")
    private String customerName;

    /** Номер места. Должен быть больше 0. */
    @Min(value = 1, message = "Номер места должен быть больше 0")
    private int seatNumber;
}