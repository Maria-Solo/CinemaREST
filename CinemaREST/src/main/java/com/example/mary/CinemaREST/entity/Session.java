package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long movieId;
    public LocalDateTime startTime;
    public int hallNumber;
    public int totalSeats;
}
