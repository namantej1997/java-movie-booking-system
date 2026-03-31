package com.example.bookingSystem.entity;

import com.example.bookingSystem.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private SeatStatus status;
    @Version
    private Long version;
    @ManyToOne
    private Show show;
}
