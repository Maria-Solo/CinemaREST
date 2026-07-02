package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
        session.setMovie(this);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        session.setMovie(null);
    }

    public String title;
    public String genre;
    public int durationMinutes;
}
