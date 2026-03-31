package com.example.bookingSystem.strategy;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BulkBookingDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(List<Seat> seats, Show show) {
        if (seats.size() >= 3) {
            // Logic: 50% off only the 3rd ticket
            BigDecimal thirdTicketPrice = seats.get(2).getPrice();
            return thirdTicketPrice.multiply(new BigDecimal("0.50"));
        }
        return BigDecimal.ZERO;
    }
}
