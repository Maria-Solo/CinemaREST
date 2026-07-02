package com.example.mary.CinemaREST.dto;

import lombok.Data;

@Data
public class MovieResponseDTO {
    private Long id;
    private String title;
    private String genre;
    private int durationMinutes;
}
