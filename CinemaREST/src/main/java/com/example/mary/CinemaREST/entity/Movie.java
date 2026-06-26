package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name="movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    public String title;

    @NotBlank(message = "Жанр не может быть пустым")
    public String genre;

    @Min(value = 1, message = "Длительность фильма должна быть больше 0 минут")
    public int durationMinutes;
}
