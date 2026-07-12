package com.example.mary.cinema.exception;

/**
 * Исключение, выбрасываемое при попытке забронировать уже занятое место.
 */
public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}