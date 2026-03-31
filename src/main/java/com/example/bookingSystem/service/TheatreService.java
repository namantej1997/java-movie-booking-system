package com.example.bookingSystem.service;

import com.example.bookingSystem.entity.Seat;
import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.enums.SeatStatus;
import com.example.bookingSystem.repository.SeatRepository;
import com.example.bookingSystem.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public Show createShowWithInventory(Show show, int rowCount, int seatsPerRow, BigDecimal basePrice) {
        Show savedShow = showRepository.save(show);
        List<Seat> inventory = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            for (int j = 1; j <= seatsPerRow; j++) {
                Seat seat = new Seat();
                seat.setSeatNumber((char)(64 + i) + String.valueOf(j));
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setPrice(basePrice);
                seat.setShow(savedShow);
                inventory.add(seat);
            }
        }
        seatRepository.saveAll(inventory);
        return savedShow;
    }

    @Transactional
    public void updateInventoryStatus(Long showId, List<String> seatNumbers, SeatStatus newStatus) {
        List<Seat> seats = seatRepository.findByShowAndSeatNumbers(showId, seatNumbers);
        seats.forEach(seat -> seat.setStatus(newStatus));
        seatRepository.saveAll(seats);
    }
}
