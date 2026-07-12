package com.example.mary.cinema;

import com.example.mary.cinema.dto.TicketRequestDTO;
import com.example.mary.cinema.entity.Movie;
import com.example.mary.cinema.entity.Session;
import com.example.mary.cinema.entity.Ticket;
import com.example.mary.cinema.repository.MovieRepository;
import com.example.mary.cinema.repository.SessionRepository;
import com.example.mary.cinema.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для TicketController.
 * Покрывают позитивные и негативные сценарии бронирования билетов.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Session savedSession;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        sessionRepository.deleteAll();
        movieRepository.deleteAll();

        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(169);
        movie = movieRepository.save(movie);

        savedSession = new Session();
        savedSession.setMovie(movie);
        savedSession.setStartTime(LocalDateTime.of(2026, 7, 15, 19, 0));
        savedSession.setHallNumber(3);
        savedSession.setTotalSeats(50);
        savedSession = sessionRepository.save(savedSession);
    }

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        sessionRepository.deleteAll();
        movieRepository.deleteAll();
    }

    // ==================== ПОЗИТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    void testGetAllTickets_ReturnsOk() throws Exception {
        Ticket ticket = createTestTicket(15);
        ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sessionId").value(savedSession.getId()))
                .andExpect(jsonPath("$[0].customerName").value("Roman"))
                .andExpect(jsonPath("$[0].seatNumber").value(15));
    }

    @Test
    void testGetAllTickets_EmptyList() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetTicketById_ReturnsTicket() throws Exception {
        Ticket ticket = createTestTicket(10);
        Ticket saved = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.sessionId").value(savedSession.getId()))
                .andExpect(jsonPath("$.customerName").value("Roman"))
                .andExpect(jsonPath("$.seatNumber").value(10));
    }

    @Test
    void testBookTicket_ReturnsCreated() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Roman");
        dto.setSeatNumber(15);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value(savedSession.getId()))
                .andExpect(jsonPath("$.customerName").value("Roman"))
                .andExpect(jsonPath("$.seatNumber").value(15));
    }

    @Test
    void testBookTicket_FirstSeat_ReturnsCreated() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Alice");
        dto.setSeatNumber(1);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value(1));
    }

    @Test
    void testBookTicket_LastSeat_ReturnsCreated() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Bob");
        dto.setSeatNumber(50);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value(50));
    }

    @Test
    void testCancelBooking_ReturnsNoContent() throws Exception {
        Ticket ticket = createTestTicket(20);
        Ticket saved = ticketRepository.save(ticket);

        mockMvc.perform(delete("/api/tickets/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tickets/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBookMultipleTicketsOnDifferentSeats_AllSucceed() throws Exception {
        TicketRequestDTO dto1 = new TicketRequestDTO();
        dto1.setSessionId(savedSession.getId());
        dto1.setCustomerName("User1");
        dto1.setSeatNumber(5);

        TicketRequestDTO dto2 = new TicketRequestDTO();
        dto2.setSessionId(savedSession.getId());
        dto2.setCustomerName("User2");
        dto2.setSeatNumber(10);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());
    }

    // ==================== НЕГАТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    void testGetTicketByInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void testCancelBooking_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void testBookTicket_EmptyCustomerName_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("");
        dto.setSeatNumber(15);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testBookTicket_NullCustomerName_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName(null);
        dto.setSeatNumber(15);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testBookTicket_SeatNumberZero_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Roman");
        dto.setSeatNumber(0);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testBookTicket_NegativeSeatNumber_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Roman");
        dto.setSeatNumber(-1);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testBookTicket_SeatExceedsTotalSeats_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("Roman");
        dto.setSeatNumber(51); // totalSeats = 50

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBookTicket_DuplicateSeat_ReturnsConflict() throws Exception {
        // Бронируем первое место
        Ticket ticket = createTestTicket(25);
        ticketRepository.save(ticket);

        // Пытаемся забронировать то же место повторно
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(savedSession.getId());
        dto.setCustomerName("AnotherUser");
        dto.setSeatNumber(25);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Seat already booked"));
    }

    @Test
    void testBookTicket_NonExistentSession_ReturnsNotFound() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(999L);
        dto.setCustomerName("Roman");
        dto.setSeatNumber(15);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found"));
    }

    @Test
    void testBookTicket_NullSessionId_ReturnsBadRequest() throws Exception {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSessionId(null);
        dto.setCustomerName("Roman");
        dto.setSeatNumber(15);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private Ticket createTestTicket(int seatNumber) {
        Ticket ticket = new Ticket();
        ticket.setSession(savedSession);
        ticket.setCustomerName("Roman");
        ticket.setSeatNumber(seatNumber);
        return ticket;
    }
}