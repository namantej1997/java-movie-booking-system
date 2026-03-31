package com.example.bookingSystem.controller;

import com.example.bookingSystem.entity.Show;
import com.example.bookingSystem.enums.SeatStatus;
import com.example.bookingSystem.repository.ShowRepository;
import com.example.bookingSystem.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1/theatre-admin")
@RequiredArgsConstructor
public class TheatreAdminController {

    private final TheatreService theatreService;
    private final ShowRepository showRepository;


    @PostMapping("/shows")
    public ResponseEntity<Show> createShow(@RequestBody Show show,
                                           @RequestParam int rows,
                                           @RequestParam int seatsPerRow,
                                           @RequestParam BigDecimal basePrice) {
        return ResponseEntity.ok(theatreService.createShowWithInventory(show, rows, seatsPerRow,basePrice));
    }

    @PatchMapping("/inventory/status")
    public ResponseEntity<String> updateInventory(
            @RequestParam Long showId,
            @RequestBody List<String> seatNumbers,
            @RequestParam SeatStatus status) {

        theatreService.updateInventoryStatus(showId, seatNumbers, status);
        return ResponseEntity.ok("Inventory updated successfully");
    }


    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long showId) {
        showRepository.deleteById(showId);
        return ResponseEntity.noContent().build();
    }
}
