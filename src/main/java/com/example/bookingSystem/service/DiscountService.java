package com.example.bookingSystem.service;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DiscountService {
    public BigDecimal calculateFinalPrice(List<Seat> seats, Show show) {
        BigDecimal total = seats.stream()
                .map(s -> s.getPrice() != null ? s.getPrice() : BigDecimal.ZERO) // Null-safe check
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (isAfternoonShow(show)) {
            total = total.multiply(new BigDecimal("0.80"));
        }

        if (seats.size() >= 3) {
            BigDecimal thirdSeatPrice = seats.get(2).getPrice();
            BigDecimal discountAmount = thirdSeatPrice.multiply(new BigDecimal("0.50"));
            total = total.subtract(discountAmount);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isAfternoonShow(Show show) {
        int hour = show.getStartTime().getHour();
        return hour >= 12 && hour <= 16;
    }
}
