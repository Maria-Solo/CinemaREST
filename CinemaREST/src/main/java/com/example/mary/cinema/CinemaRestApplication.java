package com.example.mary.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Cinema REST API.
 * Точка входа Spring Boot приложения для управления кинотеатром.
 * Предоставляет REST API для работы с фильмами, сеансами и билетами.
 */
@SpringBootApplication
public class CinemaRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaRestApplication.class, args);
    }
}