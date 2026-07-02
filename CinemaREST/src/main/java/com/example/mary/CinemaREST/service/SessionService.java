package com.example.mary.CinemaREST.service;

import com.example.mary.CinemaREST.entity.Session;
import com.example.mary.CinemaREST.exception.SessionNotFoundException;
import com.example.mary.CinemaREST.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session findSessionById(Long id) {
        Optional <Session> foundSession = sessionRepository.findById(Math.toIntExact(id));
        return foundSession.orElseThrow(() -> new SessionNotFoundException("Сеанс с таким id не существует"));
    }

    public int getFreeSeats(Long sessionId) {
       Session foundSession = findSessionById(sessionId);
       return foundSession.getTotalSeats() - foundSession.getTickets().size();
    }

    @Transactional
    public void deleteSession(Long id) {
        sessionRepository.deleteById(Math.toIntExact(id));
    }

    @Transactional
    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

}
