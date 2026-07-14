package com.example.mary.cinema.repository;

import com.example.mary.cinema.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Session}.
 * Предоставляет стандартные CRUD-операции и поиск сеанса по ID.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Находит сеанс по его идентификатору.
     *
     * @param id идентификатор сеанса
     * @return {@link Optional}, содержащий сеанс, если он найден
     */
    Optional<Session> findSessionById(Long id);
}