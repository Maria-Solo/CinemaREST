package com.example.mary.CinemaREST;

import com.example.mary.CinemaREST.entity.Movie;
import com.example.mary.CinemaREST.repository.MovieRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/// Рад, что все тесты успешно прошли и теперь сияют зелёным цветом!

// Поднимаем настоящий веб-сервер на случайном порту
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieRestAssuredTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        // Говорим REST Assured, на какой порт отправлять реальные HTTP-запросы
        RestAssured.port = port;

        // Очищаем базу перед каждым тестом
        movieRepository.deleteAll();
    }

    @Test
    public void testGetAllMoviesWithRestAssured() {
        // Сначала сохраняем тестовый фильм в базу в Docker через репозиторий
        Movie movie = new Movie();
        movie.setTitle("Начало");
        movie.setGenre("Триллер");
        movie.setDurationMinutes(148);
        movieRepository.save(movie);

        // Отправляем реальный HTTP GET запрос с помощью REST Assured
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/movies")
                .then()
                .statusCode(200) // Проверяем HTTP статус ответа
                .body("$", hasSize(1)) // Проверяем, что вернулся массив из 1 элемента
                .body("[0].title", equalTo("Начало")) // Проверяем поле внутри JSON-массива
                .body("[0].genre", equalTo("Триллер"));
    }

    @Test
    public void testCreateMovieWithRestAssured() {
        // 1. Создаем объект фильма, который хотим отправить на сервер
        Movie newMovie = new Movie();
        newMovie.setTitle("Terminator 2: Judgment Day");
        newMovie.setGenre("Фантастика");
        newMovie.setDurationMinutes(137);

        // 2. Отправляем реальный HTTP POST запрос
        given()
                .contentType(ContentType.JSON) // Указываем тип контента JSON
                .body(newMovie) // REST Assured сам автоматически превратит объект в JSON-строку!
                .when()
                .post("/api/movies") // Шлем запрос на эндпоинт контроллера
                .then()
                .statusCode(200); // Проверяем, что сервер успешно сохранил фильм и вернул 200 OK

        // 3. Дополнительно проверяем базу данных репозиторием
        long count = movieRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(1, count, "Фильм должен успешно записаться в БД Docker!");
    }

    @Test
    public void testCreateMovieValidationFailed(){
        Movie invalidMovie = new Movie();
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
                //.body("title", equalTo("Название фильма не может быть пустым"))
    }
    /*
    Когда валидация падает, ваш GlobalExceptionHandler формирует JSON следующего вида:

    json{
  "message": "Validation failed",
  "details": {
    "title": "Название фильма не может быть пустым",
    "genre": "Жанр не может быть пустым",
    "durationMinutes": "Длительность фильма должна быть больше 0 минут"
  }
}
REST Assured парсит этот JSON и с помощью синтаксиса details.genre
заходит внутрь объекта details и берет значение нужного ключа для сравнения.

     */

    @Test
    public void testGetMovieByInvalidId() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/movies/999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Фильм с таким id не существует"));
                //.body("error", equalTo("Фильм с таким id не существует"));
    }
}
/*
Как это работает вместе с Docker?
Когда вы запускаете этот тест кнопкой Run в IDEA:

Spring Boot поднимает локальный сервер Tomcat на вашем ПК.
Spring Boot подключается к базе данных PostgreSQL в Docker через порт 5434 (так как настройки берутся из вашего application.yml).
REST Assured шлет HTTP-запрос на локальный Tomcat, тот идет в Docker-базу, забирает данные и возвращает ответ.
 */
