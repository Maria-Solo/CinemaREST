package com.example.mary.CinemaREST.dto;

import com.example.mary.CinemaREST.entity.Movie;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRequestDTO {
    //private Long movieId;
    /// добавить валидацию на то, что фильм должен существовать
    private Movie movie;
    private LocalDateTime startTime;

    @Min(value = 1, message = "Номер зала должен быть больше 0")
    private int hallNumber;

    @Min(value = 1, message = "Количество мест должно быть больше 0")
    @Max(value = 300, message = "Количество мест не может превышать 300")
    private int totalSeats;
}
