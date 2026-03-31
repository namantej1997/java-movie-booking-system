package com.example.bookingSystem.controller;

import com.example.bookingSystem.dto.BookingRequest;
import com.example.bookingSystem.entity.Booking;
import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.repository.ShowRepository;
import com.example.bookingSystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ShowRepository showRepository;

    @GetMapping("/browse")
    public ResponseEntity<List<Show>> browseShows(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return ResponseEntity.ok(showRepository.findShowsByCityAndDate(city, startOfDay, endOfDay));
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.getShowId(),
                request.getSeatIds(),
                request.getUserEmail()
        );
        return ResponseEntity.ok(booking);
    }
}