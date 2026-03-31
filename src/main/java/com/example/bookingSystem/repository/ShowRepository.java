package com.example.bookingSystem.repository;

import com.example.bookingSystem.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query("SELECT s FROM Show s JOIN s.theatre t " +
            "WHERE t.city = :city " +
            "AND s.startTime >= :startOfDay " +
            "AND s.startTime <= :endOfDay")
    List<Show> findShowsByCityAndDate(@Param("city") String city,
                                      @Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);

    List<Show> findByTheatreId(Long theatreId);
}
