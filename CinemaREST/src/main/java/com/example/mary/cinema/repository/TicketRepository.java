package com.example.mary.cinema.repository;

import com.example.mary.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Ticket}.
 * Предоставляет стандартные CRUD-операции и методы для проверки занятости мест.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Проверяет, занято ли указанное место на данном сеансе.
     *
     * @param sessionId  идентификатор сеанса
     * @param seatNumber номер места
     * @return {@code true}, если место уже забронировано
     */
    boolean existsBySessionIdAndSeatNumber(Long sessionId, int seatNumber);

    /**
     * Возвращает все билеты для указанного сеанса.
     *
     * @param sessionId идентификатор сеанса
     * @return список билетов, купленных на данный сеанс
     */
    List<Ticket> findBySessionId(Long sessionId);
}