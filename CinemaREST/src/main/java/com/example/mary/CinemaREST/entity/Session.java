package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    public Movie movie;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ticket> tickets = new ArrayList<>();

    public void addTicket(Ticket ticket){
        tickets.add(ticket);
        ticket.setSession(this);
    }

    public void removeTicket(Ticket ticket){
        tickets.remove(ticket);
        ticket.setSession(null);
    }

    public LocalDateTime startTime;
    public int hallNumber;
    public int totalSeats;
}
/*
Правильный mappedBy: Это поле всегда принимает значение названия переменной в Java.
В исправленном Ticket переменная называется session, поэтому в Session мы пишем mappedBy = "session"
 */