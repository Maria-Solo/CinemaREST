package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long sessionId;
    public String customerName;
    public int seatNumber;
}
