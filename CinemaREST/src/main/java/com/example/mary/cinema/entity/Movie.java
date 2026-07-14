package com.example.mary.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность фильма.
 * Хранит информацию о фильме: название, жанр, длительность.
 * Связана отношением один-ко-многим с сущностью {@link Session}.
 */
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Session> sessions = new ArrayList<>();

    /**
     * Добавляет сеанс к фильму, устанавливая двустороннюю связь.
     *
     * @param session сеанс, который нужно добавить
     */
    public void addSession(Session session) {
        sessions.add(session);
        session.setMovie(this);
    }

    /**
     * Удаляет сеанс у фильма, разрывая двустороннюю связь.
     *
     * @param session сеанс, который нужно удалить
     */
    public void removeSession(Session session) {
        sessions.remove(session);
        session.setMovie(null);
    }
}
