package com.example.mary.CinemaREST.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketRequestDTO {
    private Long sessionId;

    @NotBlank(message = "Имя не может быть пустым")
    private String customerName;

    @Min(value = 1, message = "Номер места должен быть больше 0")
    private Integer seatNumber;
}
