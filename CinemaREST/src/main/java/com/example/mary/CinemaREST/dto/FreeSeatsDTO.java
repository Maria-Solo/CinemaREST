package com.example.mary.CinemaREST.dto;

import lombok.Data;

@Data
public class FreeSeatsDTO {
    private Long sessionId;
    private int totalSeats;
    private int occupiedSeats;
    private int freeSeats;
}
