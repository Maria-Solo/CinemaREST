package com.example.mary.cinema.service;

import com.example.mary.cinema.dto.TicketRequestDTO;
import com.example.mary.cinema.entity.Session;
import com.example.mary.cinema.entity.Ticket;
import com.example.mary.cinema.exception.SeatAlreadyBookedException;
import com.example.mary.cinema.exception.TicketNotFoundException;
import com.example.mary.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с билетами.
 * Содержит бизнес-логику бронирования, получения и отмены билетов.
 */
@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SessionService sessionService;

    public TicketService(TicketRepository ticketRepository, SessionService sessionService) {
        this.ticketRepository = ticketRepository;
        this.sessionService = sessionService;
    }

    /**
     * Возвращает список всех билетов.
     *
     * @return список всех забронированных билетов
     */
    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Находит билет по идентификатору.
     *
     * @param id идентификатор билета
     * @return найденный билет
     * @throws TicketNotFoundException если билет с указанным id не найден
     */
    public Ticket findTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
    }

    /**
     * Бронирует билет на сеанс.
     * Проверяет, что:
     * <ul>
     *   <li>сеанс существует;</li>
     *   <li>номер места не превышает общее количество мест в зале;</li>
     *   <li>место ещё не занято.</li>
     * </ul>
     *
     * @param dto данные для бронирования билета
     * @return сохранённый билет
     * @throws SeatAlreadyBookedException если место уже занято
     */
    @Transactional
    public Ticket book(TicketRequestDTO dto) {
        Session session = sessionService.findSessionById(dto.getSessionId());

        if (dto.getSeatNumber() > session.getTotalSeats()) {
            throw new IllegalArgumentException(
                    "Номер места " + dto.getSeatNumber() + " превышает общее количество мест в зале (" + session.getTotalSeats() + ")");
        }

        if (ticketRepository.existsBySessionIdAndSeatNumber(dto.getSessionId(), dto.getSeatNumber())) {
            throw new SeatAlreadyBookedException("Seat already booked");
        }

        Ticket ticket = new Ticket();
        ticket.setSession(session);
        ticket.setCustomerName(dto.getCustomerName());
        ticket.setSeatNumber(dto.getSeatNumber());

        return ticketRepository.save(ticket);
    }

    /**
     * Отменяет бронирование билета по идентификатору.
     *
     * @param id идентификатор билета для отмены
     * @throws TicketNotFoundException если билет с указанным id не найден
     */
    @Transactional
    public void cancelBooking(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }
}