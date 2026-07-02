package com.example.mary.CinemaREST.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MovieRequestDTO {

    @NotBlank(message = "Название фильма не может быть пустым")
    //@Size(min = 2, max = 100, message = "Название фильма должно быть от 2 до 100 символов")
    private String title;

    @NotBlank(message = "Жанр не может быть пустым")
    private String genre;

    @Min(value = 1, message = "Длительность фильма должна быть больше 0 минут")
    @Max(value = 600, message = "Длительность фильма не может превышать 600 минут")
    private int durationMinutes;

}
