package com.example.bookingSystem.repository;

import com.example.bookingSystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserEmailOrderByBookingTimeDesc(String userEmail);


    @Query("SELECT b FROM Booking b WHERE b.bookingTime BETWEEN :start AND :end")
    List<Booking> findBookingsInTimeRange(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Booking b JOIN b.seats s WHERE s.show.id = :showId")
    List<Booking> findAllBookingsByShowId(@Param("showId") Long showId);
}