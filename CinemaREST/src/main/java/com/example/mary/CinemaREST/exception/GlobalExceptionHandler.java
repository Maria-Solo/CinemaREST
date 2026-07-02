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
        public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, Object> response = new HashMap<>();

            response.put("message", "Validation failed");

            // Собираем все ошибки: имя поля -> текст ошибки

            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            // Кладим карту с деталями внутрь основного ответа под ключом "details"
            response.put("details", errors);

            // Возвращаем статус 400 Bad Request и карту с ошибками
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MovieNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleMovieNotFoundException(MovieNotFoundException ex) {
            Map<String, String> response = new HashMap<>();

            // Кладим текст "Фильм с таким id не существует" (из вашего MovieService) под ключ "message"
            response.put("message", ex.getMessage());

            // Клиент получит JSON: {"message": "Фильм с таким id не существует"} со статусом 404
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // 2. Пример перехвата любой другой непредвиденной ошибки сервера
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Произошла внутренняя ошибка сервера");
            error.put("details", ex.getMessage());

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(SessionNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleSessionNotFoundException(SessionNotFoundException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }
