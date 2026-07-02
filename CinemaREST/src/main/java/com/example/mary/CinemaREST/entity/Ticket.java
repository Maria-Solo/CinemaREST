package com.example.mary.CinemaREST.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)

    //Создаем колонку в таблице tickets. После name - как она будет называться
    @JoinColumn(name = "session_id")
    private Session session;
    public String customerName;
    public int seatNumber;
}
/*
Владелец = Тот, кто создает колонку в БД. У него пишем @JoinColumn.
Ведомый = Тот, кто просто пользуется этой связью в Java. У него пишем mappedBy.

Когда вы пишете @ManyToOne, Hibernate ожидает увидеть полноценный связанный класс (Session).
Если оставить Long, Hibernate выдаст ошибку AnnotationException.
 */