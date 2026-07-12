package com.example.mary.cinema.exception;

/**
 * Исключение, выбрасываемое при попытке получить или удалить несуществующий билет.
 */
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}