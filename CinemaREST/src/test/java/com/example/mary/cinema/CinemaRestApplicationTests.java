package com.example.mary.cinema;

import com.example.mary.cinema.dto.MovieRequestDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaRestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetAllMovies() throws Exception {
        movieRepository.deleteAll();

        Movie movie1 = new Movie();
        movie1.setTitle("The Lord of the Rings: The Two Towers");
        movie1.setGenre("Adventure");
        movie1.setDurationMinutes(136);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Star Wars: Episode V - The Empire Strikes Back");
        movie2.setGenre("Space Sci-Fi");
        movie2.setDurationMinutes(124);
        movieRepository.save(movie2);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings: The Two Towers"))
                .andExpect(jsonPath("$[1].title").value("Star Wars: Episode V - The Empire Strikes Back"));
    }

    @Test
    void testGetMovieById() throws Exception {
        Movie testMovie = new Movie();
        testMovie.setTitle("Начало");
        testMovie.setGenre("Триллер");
        testMovie.setDurationMinutes(148);
        Movie savedMovie = movieRepository.save(testMovie);

        mockMvc.perform(get("/api/movies/" + savedMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMovie.getId()))
                .andExpect(jsonPath("$.title").value("Начало"));
    }

    @Test
    void testDeleteMovie() throws Exception {
        Movie testMovie = new Movie();
        testMovie.setTitle("Фильм для удаления");
        testMovie.setGenre("Драма");
        testMovie.setDurationMinutes(120);
        Movie savedMovie = movieRepository.save(testMovie);

        long id = savedMovie.getId();
        assertTrue(movieRepository.existsById(id), "Фильм должен существовать до удаления");

        mockMvc.perform(delete("/api/movies/" + id))
                .andExpect(status().isNoContent());

        assertFalse(movieRepository.existsById(id), "Фильм должен быть удален из базы данных!");
    }

    @Test
    void testCreateAndGetMovie() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO();
        dto.setTitle("Interstellar");
        dto.setGenre("Sci-Fi");
        dto.setDurationMinutes(169);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Interstellar"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));

        assertFalse(movieRepository.findAll().isEmpty(), "Данные не записались в базу!");
    }

    // ==================== НЕГАТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    void testGetMovieByInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    void testDeleteMovie_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/movies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    void testCreateMovie_EmptyTitle_ReturnsBadRequest() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO();
        dto.setTitle("");
        dto.setGenre("Sci-Fi");
        dto.setDurationMinutes(120);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testCreateMovie_DurationZero_ReturnsBadRequest() throws Exception {
        MovieRequestDTO dto = new MovieRequestDTO();
        dto.setTitle("Test");
        dto.setGenre("Sci-Fi");
        dto.setDurationMinutes(0);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}