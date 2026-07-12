package com.example.mary.cinema.exception;

/**
 * Исключение, выбрасываемое при попытке получить или удалить несуществующий фильм.
 */
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }
}