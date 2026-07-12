package com.example.mary.cinema.repository;

import com.example.mary.cinema.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Movie}.
 * Предоставляет стандартные CRUD-операции и поиск фильма по ID.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Находит фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     * @return {@link Optional}, содержащий фильм, если он найден
     */
    Optional<Movie> findMovieById(Long id);
}