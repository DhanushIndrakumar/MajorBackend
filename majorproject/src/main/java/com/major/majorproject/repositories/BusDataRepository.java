package com.major.majorproject.repositories;

import com.major.majorproject.entities.BusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BusDataRepository extends JpaRepository<BusData,Integer> {

    // Custom query to find buses by source, destination, and departure date
    @Query("SELECT b FROM BusData b WHERE b.source = :source AND b.destination = :destination AND b.departureDate = :departureDate")
    List<BusData> findBySourceAndDestinationAndDate(@Param("source") String source,
                                                    @Param("destination") String destination,
                                                    @Param("departureDate") Date departureDate);
}
