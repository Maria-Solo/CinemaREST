package com.example.mary.CinemaREST.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

    @RestControllerAdvice // Говорит Spring, что этот класс перехватывает ошибки со всех контроллеров
    public class GlobalExceptionHandler {

        // 1. Перехватываем ошибки валидации (когда прислали некорректный JSON)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();

            // Собираем все ошибки: имя поля -> текст ошибки
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            // Возвращаем статус 400 Bad Request и карту с ошибками
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // 2. Пример перехвата любой другой непредвиденной ошибки сервера
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Произошла внутренняя ошибка сервера");
            error.put("details", ex.getMessage());

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(MovieNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleMovieNotFoundException(MovieNotFoundException ex){

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
