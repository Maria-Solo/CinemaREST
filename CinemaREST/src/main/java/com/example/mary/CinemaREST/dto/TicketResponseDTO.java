package com.example.mary.CinemaREST.dto;

import lombok.Data;

@Data
public class TicketResponseDTO {
    private Long id;
    private Long sessionId;
    private String customerName;
    private Integer seatNumber;
}
