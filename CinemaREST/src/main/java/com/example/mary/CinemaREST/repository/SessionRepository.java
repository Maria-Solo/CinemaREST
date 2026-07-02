package com.example.mary.CinemaREST.repository;

import com.example.mary.CinemaREST.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
}
