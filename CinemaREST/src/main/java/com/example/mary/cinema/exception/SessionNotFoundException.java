package com.example.mary.cinema.exception;

/**
 * Исключение, выбрасываемое при попытке получить или удалить несуществующий сеанс.
 */
public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}