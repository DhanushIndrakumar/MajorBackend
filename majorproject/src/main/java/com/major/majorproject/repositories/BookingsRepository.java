package com.major.majorproject.repositories;

import com.major.majorproject.entities.Bookings;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingsRepository extends JpaRepository<Bookings,Integer> {
    // Derived Query Method
    List<Bookings> findByUser_UserId(int userId);

    // OR: Custom JPQL Query
    @Query("SELECT b FROM Bookings b WHERE b.user.userId = :userId")
    List<Bookings> findBookingsByUserId(@Param("userId") int userId);

    // Derived Query Method for Deletion
    void deleteByUser_UserId(int userId);

    // Custom JPQL Query for Deletion
    @Modifying
    @Transactional
    @Query("DELETE FROM Bookings b WHERE b.user.userId = :userId")
    void deleteBookingsByUserId(@Param("userId") int userId);

    void deleteByBusData_BusId(int busId);

    Bookings findByBusData_BusId(int busId);
}
