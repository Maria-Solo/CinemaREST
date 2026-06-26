package com.example.mary.CinemaREST.repository;

import com.example.mary.CinemaREST.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findMovieById(Long id);
}
