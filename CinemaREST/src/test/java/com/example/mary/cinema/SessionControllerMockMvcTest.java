package com.example.mary.cinema;

import com.example.mary.cinema.dto.SessionRequestDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.entity.Session;
import com.example.mary.cinema.repository.MovieRepository;
import com.example.mary.cinema.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для SessionController.
 * Покрывают позитивные и негативные сценарии работы с сеансами.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie savedMovie;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        movieRepository.deleteAll();

        savedMovie = new Movie();
        savedMovie.setTitle("Interstellar");
        savedMovie.setGenre("Sci-Fi");
        savedMovie.setDurationMinutes(169);
        savedMovie = movieRepository.save(savedMovie);
    }

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        movieRepository.deleteAll();
    }

    // ==================== ПОЗИТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    void testGetAllSessions_ReturnsOk() throws Exception {
        Session session = createTestSession();
        sessionRepository.save(session);

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].movieId").value(savedMovie.getId()))
                .andExpect(jsonPath("$[0].hallNumber").value(3))
                .andExpect(jsonPath("$[0].totalSeats").value(50));
    }

    @Test
    void testGetAllSessions_EmptyList() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetSessionById_ReturnsSession() throws Exception {
        Session session = createTestSession();
        Session saved = sessionRepository.save(session);

        mockMvc.perform(get("/api/sessions/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.movieId").value(savedMovie.getId()))
                .andExpect(jsonPath("$.hallNumber").value(3));
    }

    @Test
    void testCreateSession_ReturnsCreated() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(savedMovie.getId());
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(3);
        dto.setTotalSeats(50);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieId").value(savedMovie.getId()))
                .andExpect(jsonPath("$.hallNumber").value(3))
                .andExpect(jsonPath("$.totalSeats").value(50));
    }

    @Test
    void testDeleteSession_ReturnsNoContent() throws Exception {
        Session session = createTestSession();
        Session saved = sessionRepository.save(session);

        mockMvc.perform(delete("/api/sessions/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/sessions/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFreeSeats_NoTickets() throws Exception {
        Session session = createTestSession();
        session.setTotalSeats(100);
        Session saved = sessionRepository.save(session);

        mockMvc.perform(get("/api/sessions/" + saved.getId() + "/free-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(saved.getId()))
                .andExpect(jsonPath("$.totalSeats").value(100))
                .andExpect(jsonPath("$.occupiedSeats").value(0))
                .andExpect(jsonPath("$.freeSeats").value(100));
    }

    // ==================== НЕГАТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    void testGetSessionByInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/sessions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found"));
    }

    @Test
    void testDeleteSession_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/sessions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found"));
    }

    @Test
    void testGetFreeSeats_InvalidSessionId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/sessions/999/free-seats"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found"));
    }

    @Test
    void testCreateSession_NonExistentMovie_ReturnsNotFound() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(999L);
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(3);
        dto.setTotalSeats(50);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    void testCreateSession_HallNumberZero_ReturnsBadRequest() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(savedMovie.getId());
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(0);
        dto.setTotalSeats(50);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testCreateSession_TotalSeatsExceeds300_ReturnsBadRequest() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(savedMovie.getId());
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(1);
        dto.setTotalSeats(301);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testCreateSession_TotalSeatsZero_ReturnsBadRequest() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(savedMovie.getId());
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(1);
        dto.setTotalSeats(0);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testCreateSession_MissingMovieId_ReturnsBadRequest() throws Exception {
        SessionRequestDTO dto = new SessionRequestDTO();
        dto.setMovieId(null);
        dto.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        dto.setHallNumber(3);
        dto.setTotalSeats(50);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private Session createTestSession() {
        Session session = new Session();
        session.setMovie(savedMovie);
        session.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        session.setHallNumber(3);
        session.setTotalSeats(50);
        return session;
    }
}