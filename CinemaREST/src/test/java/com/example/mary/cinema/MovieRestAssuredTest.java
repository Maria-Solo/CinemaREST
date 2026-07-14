package com.example.mary.cinema;

import com.example.mary.cinema.dto.MovieRequestDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.repository.MovieRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieRestAssuredTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        movieRepository.deleteAll();
    }

    @Test
    void testGetAllMoviesWithRestAssured() {
        Movie movie = new Movie();
        movie.setTitle("Начало");
        movie.setGenre("Триллер");
        movie.setDurationMinutes(148);
        movieRepository.save(movie);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/movies")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("Начало"))
                .body("[0].genre", equalTo("Триллер"));
    }

    @Test
    void testCreateMovieWithRestAssured() {
        MovieRequestDTO newMovie = new MovieRequestDTO();
        newMovie.setTitle("Terminator 2: Judgment Day");
        newMovie.setGenre("Фантастика");
        newMovie.setDurationMinutes(137);

        given()
                .contentType(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(201);

        long count = movieRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(1, count, "Фильм должен успешно записаться в БД!");
    }

    @Test
    void testCreateMovieValidationFailed() {
        MovieRequestDTO invalidMovie = new MovieRequestDTO();
        invalidMovie.setTitle("");
        invalidMovie.setGenre("");
        invalidMovie.setDurationMinutes(0);

        given()
                .contentType(ContentType.JSON)
                .body(invalidMovie)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(400)
                .body("message", equalTo("Validation failed"))
                .body("details.title", equalTo("Название фильма не может быть пустым"))
                .body("details.genre", equalTo("Жанр не может быть пустым"))
                .body("details.durationMinutes", equalTo("Длительность фильма должна быть больше 0 минут"));
    }

    @Test
    void testGetMovieByInvalidId() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/movies/999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Movie not found"));
    }
}