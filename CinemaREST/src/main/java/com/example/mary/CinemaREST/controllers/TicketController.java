package com.example.mary.CinemaREST.controllers;

import com.example.mary.CinemaREST.dto.TicketRequestDTO;
import com.example.mary.CinemaREST.dto.TicketResponseDTO;
import com.example.mary.CinemaREST.entity.Session;
import com.example.mary.CinemaREST.entity.Ticket;
import com.example.mary.CinemaREST.service.SessionService;
import com.example.mary.CinemaREST.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private SessionService sessionService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping()
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.findAllTickets()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TicketResponseDTO convertToDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setSessionId(ticket.getSession().getId());
        dto.setCustomerName(ticket.getCustomerName());
        dto.setSeatNumber(ticket.getSeatNumber());
        return dto;
    }

    public Ticket convertToEntity(TicketRequestDTO dto){
        Ticket ticket = new Ticket();
        Long foundSessionId = dto.getSessionId();

        Session session = sessionService.findSessionById(foundSessionId);

        ticket.setSession(session);
        ticket.setCustomerName(dto.getCustomerName());
        ticket.setSeatNumber(ticket.getSeatNumber());
        return ticket;
    }

    @GetMapping("/{id}")
    public TicketResponseDTO getTicketById(@PathVariable("id") Long id) {
        Optional <Ticket> ticket = ticketService.findTicketById(id);
        return convertToDTO(ticket);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void cancelBooking(@PathVariable("id") Long id) {
        ticketService.cancelBooking(id);
    }

    @PostMapping()
    public void bookTicket(@Valid @RequestBody TicketRequestDTO ticketRequestDTO) throws Exception {
        Ticket ticket = convertToEntity(ticketRequestDTO);
        ticketService.book(ticket);
    }

}
