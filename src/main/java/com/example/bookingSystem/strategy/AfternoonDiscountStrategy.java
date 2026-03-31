package com.example.bookingSystem.strategy;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AfternoonDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(List<Seat> seats, Show show) {
        int hour = show.getStartTime().getHour();
        if (hour >= 12 && hour <= 16) {
            return new BigDecimal("0.20"); // 20% off
        }
        return BigDecimal.ZERO;
    }
}