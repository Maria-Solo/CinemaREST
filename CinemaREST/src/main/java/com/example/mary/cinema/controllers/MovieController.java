package com.example.mary.cinema.controllers;

import com.example.mary.cinema.dto.MovieRequestDTO;
import com.example.mary.cinema.dto.MovieResponseDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с фильмами.
 * Предоставляет REST API для CRUD-операций над сущностью {@link Movie}.
 * Базовый путь: /api/movies
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Возвращает список всех фильмов.
     *
     * @return HTTP 200 со списком фильмов в формате {@link MovieResponseDTO}
     */
    @GetMapping
    public ResponseEntity<List<MovieResponseDTO>> getAllMovies() {
        List<MovieResponseDTO> movies = movieService.findAllMovies()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movies);
    }

    /**
     * Возвращает фильм по идентификатору.
     *
     * @param id идентификатор фильма
     * @return HTTP 200 с данными фильма, либо 404 если фильм не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.findMovieById(id);
        return ResponseEntity.ok(convertToResponseDTO(movie));
    }

    /**
     * Создаёт новый фильм.
     *
     * @param dto данные нового фильма (проходят валидацию)
     * @return HTTP 201 с данными созданного фильма
     */
    @PostMapping
    public ResponseEntity<MovieResponseDTO> createMovie(@Valid @RequestBody MovieRequestDTO dto) {
        Movie movie = convertToEntity(dto);
        Movie saved = movieService.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(saved));
    }

    /**
     * Удаляет фильм по идентификатору.
     *
     * @param id идентификатор фильма
     * @return HTTP 204 при успешном удалении, либо 404 если фильм не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Преобразует DTO запроса в сущность Movie.
     */
    private Movie convertToEntity(MovieRequestDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDurationMinutes(dto.getDurationMinutes());
        return movie;
    }

    /**
     * Преобразует сущность Movie в DTO ответа.
     */
    private MovieResponseDTO convertToResponseDTO(Movie movie) {
        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setDurationMinutes(movie.getDurationMinutes());
        return dto;
    }
}