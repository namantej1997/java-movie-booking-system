package com.example.bookingSystem.service;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.strategy.DiscountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final List<DiscountStrategy> strategies;

    public BigDecimal calculateFinalPrice(List<Seat> seats, Show show) {
        BigDecimal baseTotal = seats.stream()
                .map(s -> s.getPrice() != null ? s.getPrice() : BigDecimal.ZERO) // Null-safe check
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (DiscountStrategy strategy : strategies) {
            BigDecimal discount = strategy.applyDiscount(seats, show);

            // If the strategy returns a percentage (like 0.20)
            if (discount.compareTo(BigDecimal.ONE) < 0 && discount.compareTo(BigDecimal.ZERO) > 0) {
                totalDiscount = totalDiscount.add(baseTotal.multiply(discount));
            } else {
                // If the strategy returns a flat amount (like $50.00)
                totalDiscount = totalDiscount.add(discount);
            }
        }

        return baseTotal.subtract(totalDiscount).setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isAfternoonShow(Show show) {
        int hour = show.getStartTime().getHour();
        return hour >= 12 && hour <= 16;
    }
}
