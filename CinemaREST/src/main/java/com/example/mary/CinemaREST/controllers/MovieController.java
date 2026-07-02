package com.example.mary.CinemaREST.controllers;

import com.example.mary.CinemaREST.dto.MovieRequestDTO;
import com.example.mary.CinemaREST.dto.MovieResponseDTO;
import com.example.mary.CinemaREST.entity.Movie;
import com.example.mary.CinemaREST.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private  final MovieService movieService;
    @Autowired
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping()
    public List<MovieResponseDTO> getAllMovies() {
    return movieService.findAllMovies()
            .stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieResponseDTO getMovieById(@PathVariable("id") Long id){
        Movie movie = movieService.findMovieById(id);
        return convertToResponseDTO(movie);
    }

    @PostMapping()
    public void createNewMovie(@Valid @RequestBody MovieRequestDTO movieRequestDTO){
        Movie movie = convertToEntity(movieRequestDTO);
        movieService.save(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable("id") Long id) {
        movieService.delete(id);
    }

    private Movie convertToEntity(MovieRequestDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDurationMinutes(dto.getDurationMinutes());
        return movie;
    }

    // Метод перевода из Entity в ResponseDTO (для отдачи клиенту)
    private MovieResponseDTO convertToResponseDTO(Movie movie) {
        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setDurationMinutes(movie.getDurationMinutes());
        return dto;
    }

}
