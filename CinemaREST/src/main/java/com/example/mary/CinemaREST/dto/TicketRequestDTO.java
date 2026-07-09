package com.example.mary.CinemaREST.dto;

import lombok.Data;

@Data
public class TicketRequestDTO {
    private Long sessionId;
    private String customerName;
    private Integer seatNumber;
}
