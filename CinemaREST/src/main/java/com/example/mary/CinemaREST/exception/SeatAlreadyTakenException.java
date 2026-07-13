package com.example.mary.CinemaREST.exception;

public class SeatAlreadyTakenException extends RuntimeException {
    public SeatAlreadyTakenException (String message) {super(message);}
}
