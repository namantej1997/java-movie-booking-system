package com.example.bookingSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private BigDecimal totalAmount;
    private LocalDateTime bookingTime;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Seat> seats;
}
