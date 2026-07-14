package com.example.mary.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Сущность билета.
 * Хранит информацию о купленном билете: имя покупателя, номер места.
 * Связана с {@link Session} (многие-к-одному).
 */
@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Session session;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;
}