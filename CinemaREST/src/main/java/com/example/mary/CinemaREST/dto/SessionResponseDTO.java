package com.example.mary.CinemaREST.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessionResponseDTO {
    private Long id;
    private Long movieId;
    public LocalDateTime startTime;
    public int hallNumber;
    public int totalSeats;
}
