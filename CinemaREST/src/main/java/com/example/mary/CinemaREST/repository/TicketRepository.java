package com.example.mary.CinemaREST.repository;

import com.example.mary.CinemaREST.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsBySessionIdAndSeatNumber(Long sessionId, int seatNumber);

    //what's this?
    List<Ticket> id(Long id);
}
