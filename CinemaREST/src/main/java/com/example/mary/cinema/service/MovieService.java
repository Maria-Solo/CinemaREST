package com.example.mary.cinema.service;

import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.exception.MovieNotFoundException;
import com.example.mary.cinema.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с фильмами.
 * Содержит бизнес-логику создания, получения и удаления фильмов.
 */
@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Возвращает список всех фильмов.
     *
     * @return список всех фильмов в базе
     */
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Находит фильм по идентификатору.
     *
     * @param id идентификатор фильма
     * @return найденный фильм
     * @throws MovieNotFoundException если фильм с указанным id не найден
     */
    public Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }

    /**
     * Сохраняет новый фильм в базе данных.
     *
     * @param movie фильм для сохранения
     * @return сохранённый фильм с присвоенным идентификатором
     */
    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    /**
     * Удаляет фильм по идентификатору.
     *
     * @param id идентификатор фильма для удаления
     * @throws MovieNotFoundException если фильм с указанным id не найден
     */
    @Transactional
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }
}