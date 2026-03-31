package com.example.bookingSystem.service;

import com.example.bookingSystem.entity.Booking;
import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.enums.SeatStatus;
import com.example.bookingSystem.repository.BookingRepository;
import com.example.bookingSystem.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final DiscountService discountService;

    @Transactional
    public Booking createBooking(Long showId, List<Long> seatIds, String userEmail) {
        List<Seat> seats = seatRepository.findAllByIdWithLock(seatIds);

        if (seats.size() != seatIds.size()) throw new RuntimeException("Some seats not found");

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already taken");
            }
            seat.setStatus(SeatStatus.BOOKED);
        }

        Show show = seats.get(0).getShow();
        BigDecimal finalPrice = discountService.calculateFinalPrice(seats, show);

        Booking booking = new Booking();
        booking.setUserEmail(userEmail);
        booking.setSeats(seats);
        booking.setTotalAmount(finalPrice);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }
}