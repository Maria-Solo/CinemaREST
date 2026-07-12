package com.example.mary.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность киносеанса.
 * Хранит информацию о сеансе: время начала, номер зала, количество мест.
 * Связана с {@link Movie} (многие-к-одному) и {@link Ticket} (один-ко-многим).
 */
@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Movie movie;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "hall_number", nullable = false)
    private int hallNumber;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ticket> tickets = new ArrayList<>();

    /**
     * Добавляет билет к сеансу, устанавливая двустороннюю связь.
     *
     * @param ticket билет, который нужно добавить
     */
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setSession(this);
    }

    /**
     * Удаляет билет у сеанса, разрывая двустороннюю связь.
     *
     * @param ticket билет, который нужно удалить
     */
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setSession(null);
    }
}