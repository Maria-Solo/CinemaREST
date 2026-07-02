package com.example.mary.CinemaREST.repository;

import com.example.mary.CinemaREST.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsBySessionIdAndSeatNumber(Long sessionId, int seatNumber);
}
