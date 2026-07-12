package com.example.mary.cinema.controllers;

import com.example.mary.cinema.dto.FreeSeatsResponseDTO;
import com.example.mary.cinema.dto.SessionRequestDTO;
import com.example.mary.cinema.dto.SessionResponseDTO;
import com.example.mary.cinema.entity.Session;
import com.example.mary.cinema.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с киносеансами.
 * Предоставляет REST API для CRUD-операций над сущностью {@link Session},
 * а также эндпоинт для получения информации о свободных местах.
 * Базовый путь: /api/sessions
 */
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Возвращает список всех сеансов.
     *
     * @return HTTP 200 со списком сеансов в формате {@link SessionResponseDTO}
     */
    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> getAllSessions() {
        List<SessionResponseDTO> sessions = sessionService.getAllSessions()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessions);
    }

    /**
     * Возвращает сеанс по идентификатору.
     *
     * @param id идентификатор сеанса
     * @return HTTP 200 с данными сеанса, либо 404 если сеанс не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> getSessionById(@PathVariable Long id) {
        Session session = sessionService.findSessionById(id);
        return ResponseEntity.ok(convertToResponseDTO(session));
    }

    /**
     * Возвращает информацию о свободных местах на сеансе.
     *
     * @param id идентификатор сеанса
     * @return HTTP 200 с информацией о свободных, занятых и общем количестве мест
     */
    @GetMapping("/{id}/free-seats")
    public ResponseEntity<FreeSeatsResponseDTO> getFreeSeats(@PathVariable Long id) {
        FreeSeatsResponseDTO freeSeats = sessionService.getFreeSeats(id);
        return ResponseEntity.ok(freeSeats);
    }

    /**
     * Создаёт новый киносеанс.
     * Проверяет существование фильма по movieId из запроса.
     *
     * @param dto данные нового сеанса (проходят валидацию)
     * @return HTTP 201 с данными созданного сеанса
     */
    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession(@Valid @RequestBody SessionRequestDTO dto) {
        Session session = sessionService.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(session));
    }

    /**
     * Удаляет сеанс по идентификатору.
     *
     * @param id идентификатор сеанса
     * @return HTTP 204 при успешном удалении, либо 404 если сеанс не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Преобразует сущность Session в DTO ответа.
     */
    private SessionResponseDTO convertToResponseDTO(Session session) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(session.getId());
        dto.setMovieId(session.getMovie().getId());
        dto.setStartTime(session.getStartTime());
        dto.setHallNumber(session.getHallNumber());
        dto.setTotalSeats(session.getTotalSeats());
        return dto;
    }
}