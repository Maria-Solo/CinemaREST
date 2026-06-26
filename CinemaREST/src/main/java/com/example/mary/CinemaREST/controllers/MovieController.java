package com.example.mary.CinemaREST.controllers;

import com.example.mary.CinemaREST.entity.Movie;
import com.example.mary.CinemaREST.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private  final MovieService movieService;
    @Autowired
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping()
    public List<Movie> getAllMovies(){
    return movieService.findAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") Long id){
        return movieService.findMovieById(id);
    }

    @PostMapping()
    public void createNewMovie(@Valid @RequestBody Movie movie){
        movieService.save(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable("id") Long id) {
        movieService.delete(id);
    }

}
