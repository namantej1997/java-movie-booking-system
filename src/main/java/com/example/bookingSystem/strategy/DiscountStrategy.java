package com.example.bookingSystem.strategy;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountStrategy {
    BigDecimal applyDiscount(List<Seat> seats, Show show);
}
