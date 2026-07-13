package com.example.mary.CinemaREST.service;

import com.example.mary.CinemaREST.entity.Session;
import com.example.mary.CinemaREST.entity.Ticket;
import com.example.mary.CinemaREST.exception.InvalidSeatException;
import com.example.mary.CinemaREST.exception.SeatAlreadyTakenException;
import com.example.mary.CinemaREST.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private SessionService sessionService;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findTicketById(Long id) {
        Optional<Ticket> foundTicket = ticketRepository.findById(id);
        return foundTicket;
    }

    @Transactional
    public void book(Ticket ticket) {
        Long ticketSessionId = ticket.getSession().getId();
        Session session = sessionService.findSessionById(ticketSessionId);

        if (ticket.getSeatNumber() <= 0 || ticket.getSeatNumber() > session.getTotalSeats()) {
            throw new InvalidSeatException("Место №" + ticket.getSeatNumber() + " не существуетв этом зале. Всего мест: " + session.getTotalSeats());
        }

        if (ticketRepository.existsBySessionIdAndSeatNumber(ticketSessionId, ticket.getSeatNumber())){
            throw new SeatAlreadyTakenException("Место №" + ticket.getSeatNumber() + " уже забронировано другим пользователем.");
        }
            session.addTicket(ticket);
            ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelBooking(Long id){
        ticketRepository.deleteById(id);
    }



}
