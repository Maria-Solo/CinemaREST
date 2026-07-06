package com.example.mary.CinemaREST.service;

import com.example.mary.CinemaREST.entity.Ticket;
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
        ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelBooking(Long id){
        ticketRepository.deleteById(id);
    }
}
