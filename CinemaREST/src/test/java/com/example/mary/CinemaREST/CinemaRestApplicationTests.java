package com.example.mary.CinemaREST;

import com.example.mary.CinemaREST.entity.Movie;
import com.example.mary.CinemaREST.repository.MovieRepository;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaRestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

    @Test
    void testGetAllMovies() throws Exception {
        // Очищаем базу для точности теста
        movieRepository.deleteAll();

        // Сохраняем два тестовых фильма напрямую в БД
        Movie movie1 = new Movie();
        movie1.setTitle("The Lord of the Rings: The Two Towers");
        movie1.setGenre("Adventure");
        movie1.setDurationMinutes(136);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Star Wars: Episode V - The Empire Strikes Back");
        movie2.setGenre("Space Sci-Fi");
        movie2.setDurationMinutes(124);
        movieRepository.save(movie2);

        // Проверяем GET /api/movies
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                // Проверяем, что в ответе вернулся массив из двух элементов
                .andExpect(jsonPath("$.length()").value(2))
                // Проверяем, что названия фильмов соответствуют сохраненным
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings: The Two Towers"))
                .andExpect(jsonPath("$[1].title").value("Star Wars: Episode V - The Empire Strikes Back"));
    }

    @Test
    void testGetMovieById() throws Exception {
        // Создаем и сохраняем один фильм
        Movie movie = new Movie();
        movie.setTitle("Начало");
        movie.setGenre("Триллер");
        movie.setDurationMinutes(148);
        Movie savedMovie = movieRepository.save(movie); // Получаем объект с реальным ID из базы

        // Проверяем GET /api/movies/{id}
        mockMvc.perform(get("/api/movies/" + savedMovie.getId()))
                .andExpect(status().isOk())
                // Проверяем, что вернулся именно тот фильм, который мы запрашивали
                .andExpect(jsonPath("$.id").value(savedMovie.getId()))
                .andExpect(jsonPath("$.title").value("Начало"));
    }

    @Test
    void testDeleteMovie() throws Exception {
        // Создаем фильм, который будем удалять
        Movie movie = new Movie();
        movie.setTitle("Фильм для удаления");
        movie.setGenre("Драма");
        movie.setDurationMinutes(120);
        Movie savedMovie = movieRepository.save(movie);

        // Проверяем, что фильм изначально есть в базе
        long id = savedMovie.getId();
        assertTrue(movieRepository.existsById(Math.toIntExact(id)), "Фильм должен существовать до удаления");

        // Вызываем метод DELETE /api/movies/{id}
        mockMvc.perform(delete("/api/movies/" + id))
                .andExpect(status().isOk());

        // ПРОВЕРКА В БАЗЕ ДАННЫХ: проверяем, что фильма там больше нет
        assertFalse(movieRepository.existsById(Math.toIntExact(id)), "Фильм должен быть удален из базы данных контейнера!");
    }


    @Test
    void testCreateAndGetMovie() throws Exception{
        Movie testMovie = new Movie();
        testMovie.setTitle("Interstellar");
        testMovie.setGenre("Sci-Fi");
        testMovie.setDurationMinutes(169);

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(status().isOk());

        assertFalse(movieRepository.findAll().isEmpty(), "Данные не записались в базу Docker!");
        System.out.println(">>> УРА! Фильм успешно прошел через контроллер и записан в базу!");

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk());
    }

    }

