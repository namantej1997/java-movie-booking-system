package com.example.bookingSystem.client;

import com.example.bookingSystem.entity.Booking;
import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.entity.Theatre;
import com.example.bookingSystem.enums.SeatStatus;
import com.example.bookingSystem.repository.SeatRepository;
import com.example.bookingSystem.repository.ShowRepository;
import com.example.bookingSystem.repository.TheatreRepository;
import com.example.bookingSystem.service.BookingService;
import com.example.bookingSystem.service.TheatreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingSystemClient implements CommandLineRunner {

    private final TheatreRepository theatreRepository;
    private final TheatreService theatreService;
    private final ShowRepository showRepository;
    private final BookingService bookingService;
    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("--- STARTING END-TO-END BOOKING FLOW SIMULATION ---");

        // 1. [B2B FLOW] - Theatre Setup
        Theatre pvr = new Theatre();
        pvr.setName("PVR Cinemas");
        pvr.setCity("Hyderabad");
        pvr.setCountryCode("IN");
        theatreRepository.save(pvr);

        // 2. [B2B FLOW] - Create an Afternoon Show
        Show movieShow = new Show();
        movieShow.setMovieName("Inception 2");
        movieShow.setStartTime(LocalDateTime.now().withHour(14).withMinute(30));
        movieShow.setTheatre(pvr);
        movieShow.setCity("Hyderabad");
        BigDecimal ticketPrice = new BigDecimal("100.00");
        Show savedShow = theatreService.createShowWithInventory(movieShow, 3, 5,ticketPrice);
        log.info("Theatre Admin created show: {} at {}", savedShow.getMovieName(), savedShow.getStartTime());

        // 3. [B2C FLOW] - Customer Browses
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59);
        List<Show> availableShows = showRepository.findShowsByCityAndDate("Hyderabad", start, end);
        log.info("Customer found {} shows in Hyderabad for today.", availableShows.size());

        // 4. [B2C FLOW] - Customer Selects Seats
        List<Seat> seatsForShow = seatRepository.findByShowId(savedShow.getId());
        List<Long> selectedSeatIds = seatsForShow.stream()
                .limit(3)
                .map(Seat::getId)
                .collect(Collectors.toList());

        log.info("Customer selecting 3 seats for booking...");

        // 5. [TRANSACTIONAL FLOW] - Create Booking
        try {
            Booking finalBooking = bookingService.createBooking(
                    savedShow.getId(),
                    selectedSeatIds,
                    "customer@example.com"
            );

            log.info("--- BOOKING SUCCESSFUL ---");
            log.info("Booking ID: {}", finalBooking.getId());
            log.info("Total Amount Paid: ${}", finalBooking.getTotalAmount());
            log.info("Applied Discounts: Afternoon Show (20%) + 3rd Ticket (50%)");

        } catch (Exception e) {
            log.error("Booking failed due to: {}", e.getMessage());
        }

        // 6. [B2B FLOW] - Theatre Admin checks remaining inventory
        long availableSeats = seatRepository.findByShowId(savedShow.getId()).stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .count();
        log.info("Remaining available seats for this show: {}", availableSeats);
    }
}