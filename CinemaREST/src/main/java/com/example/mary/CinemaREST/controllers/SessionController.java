package com.example.mary.CinemaREST.controllers;

import com.example.mary.CinemaREST.dto.SessionRequestDTO;
import com.example.mary.CinemaREST.dto.SessionResponseDTO;
import com.example.mary.CinemaREST.entity.Session;
import com.example.mary.CinemaREST.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;


    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping()
    public List<SessionResponseDTO> getAllSessions() {
        return sessionService.getAllSessions()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    private SessionResponseDTO convertToResponseDTO(Session session) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(session.getId());
        dto.setMovieId(session.getMovie().getId());
        dto.setStartTime(session.getStartTime());
        dto.setHallNumber(session.getHallNumber());
        dto.setTotalSeats(session.getTotalSeats());
        return dto;
    }

    private Session convertToEntity(SessionRequestDTO dto){
        Session session = new Session();
        session.setMovie(dto.getMovie());
        session.setStartTime(dto.getStartTime());
        session.setHallNumber(dto.getHallNumber());
        session.setTotalSeats(dto.getTotalSeats());
        return session;
    }

    @GetMapping("/{id}")
    public SessionResponseDTO getSessionById(@PathVariable("id") Long id) {
        Session session = sessionService.findSessionById(id);
        return convertToResponseDTO(session);
    }

    @PostMapping()
    public void createSession(@Valid @RequestBody SessionRequestDTO sessionRequestDTO) {
        Session session = convertToEntity(sessionRequestDTO);
        sessionService.saveSession(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable("id") Long id){
        sessionService.deleteSession(id);
    }

    @GetMapping("/{id}/free-seats")
    public int getFreeSeats(@PathVariable("id") Long id) {
       return sessionService.getFreeSeats(id);
    }

}
