package com.example.mary.CinemaREST.service;

import com.example.mary.CinemaREST.entity.Movie;
import com.example.mary.CinemaREST.exception.MovieNotFoundException;
import com.example.mary.CinemaREST.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;


    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

    public Movie findMovieById(Long id){
        Optional<Movie> foundMovie = movieRepository.findMovieById(id);
        return foundMovie.orElseThrow(() -> new MovieNotFoundException("Фильм с таким id не существует"));
    }

    @Transactional
    public void save(Movie movie){
        movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id){
        movieRepository.deleteById(Math.toIntExact(id));
    }
}
