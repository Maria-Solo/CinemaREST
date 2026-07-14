package com.example.mary.cinema.service;

import com.example.mary.cinema.dto.FreeSeatsResponseDTO;
import com.example.mary.cinema.dto.SessionRequestDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.entity.Session;
import com.example.mary.cinema.exception.MovieNotFoundException;
import com.example.mary.cinema.exception.SessionNotFoundException;
import com.example.mary.cinema.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с киносеансами.
 * Содержит бизнес-логику создания, получения, удаления сеансов и расчёта свободных мест.
 */
@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieService movieService;

    public SessionService(SessionRepository sessionRepository, MovieService movieService) {
        this.sessionRepository = sessionRepository;
        this.movieService = movieService;
    }

    /**
     * Возвращает список всех сеансов.
     *
     * @return список всех сеансов в базе
     */
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    /**
     * Находит сеанс по идентификатору.
     *
     * @param id идентификатор сеанса
     * @return найденный сеанс
     * @throws SessionNotFoundException если сеанс с указанным id не найден
     */
    public Session findSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
    }

    /**
     * Создаёт новый сеанс на основе DTO.
     * Проверяет существование фильма по movieId из DTO.
     *
     * @param dto данные для создания сеанса
     * @return сохранённый сеанс
     * @throws MovieNotFoundException если фильм с указанным movieId не найден
     */
    @Transactional
    public Session createSession(SessionRequestDTO dto) {
        Movie movie = movieService.findMovieById(dto.getMovieId());

        Session session = new Session();
        session.setMovie(movie);
        session.setStartTime(dto.getStartTime());
        session.setHallNumber(dto.getHallNumber());
        session.setTotalSeats(dto.getTotalSeats());

        return sessionRepository.save(session);
    }

    /**
     * Рассчитывает количество свободных мест на сеансе.
     *
     * @param sessionId идентификатор сеанса
     * @return DTO с информацией о свободных местах
     * @throws SessionNotFoundException если сеанс не найден
     */
    public FreeSeatsResponseDTO getFreeSeats(Long sessionId) {
        Session session = findSessionById(sessionId);
        int totalSeats = session.getTotalSeats();
        int occupiedSeats = session.getTickets().size();
        int freeSeats = totalSeats - occupiedSeats;
        return new FreeSeatsResponseDTO(sessionId, totalSeats, occupiedSeats, freeSeats);
    }

    /**
     * Удаляет сеанс по идентификатору.
     *
     * @param id идентификатор сеанса для удаления
     * @throws SessionNotFoundException если сеанс с указанным id не найден
     */
    @Transactional
    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new SessionNotFoundException("Session not found");
        }
        sessionRepository.deleteById(id);
    }
}