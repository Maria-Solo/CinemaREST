package com.example.mary.cinema.controllers;

import com.example.mary.cinema.dto.TicketRequestDTO;
import com.example.mary.cinema.dto.TicketResponseDTO;
import com.example.mary.cinema.entity.Ticket;
import com.example.mary.cinema.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с билетами.
 * Предоставляет REST API для бронирования, получения и отмены билетов.
 * Базовый путь: /api/tickets
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Возвращает список всех забронированных билетов.
     *
     * @return HTTP 200 со списком билетов в формате {@link TicketResponseDTO}
     */
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        List<TicketResponseDTO> tickets = ticketService.findAllTickets()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    /**
     * Возвращает билет по идентификатору.
     *
     * @param id идентификатор билета
     * @return HTTP 200 с данными билета, либо 404 если билет не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.findTicketById(id);
        return ResponseEntity.ok(convertToResponseDTO(ticket));
    }

    /**
     * Бронирует новый билет на сеанс.
     * Проверяет, что место свободно, не превышает вместимость зала и сеанс существует.
     *
     * @param dto данные для бронирования (проходят валидацию)
     * @return HTTP 201 с данными забронированного билета
     */
    @PostMapping
    public ResponseEntity<TicketResponseDTO> bookTicket(@Valid @RequestBody TicketRequestDTO dto) {
        Ticket ticket = ticketService.book(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(ticket));
    }

    /**
     * Отменяет бронирование билета (удаляет билет).
     *
     * @param id идентификатор билета
     * @return HTTP 204 при успешной отмене, либо 404 если билет не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        ticketService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Преобразует сущность Ticket в DTO ответа.
     */
    private TicketResponseDTO convertToResponseDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setSessionId(ticket.getSession().getId());
        dto.setCustomerName(ticket.getCustomerName());
        dto.setSeatNumber(ticket.getSeatNumber());
        return dto;
    }
}