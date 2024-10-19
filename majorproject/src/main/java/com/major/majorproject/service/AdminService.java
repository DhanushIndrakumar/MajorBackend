package com.major.majorproject.service;


import com.major.majorproject.DTO.BusRequest;
import com.major.majorproject.DTO.CompleteBookingsDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.repositories.BookingsRepository;
import com.major.majorproject.repositories.BusDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private BusDataRepository busDataRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    public BusData addBus(BusRequest busRequest) {
        BusData busData=new BusData();
        busData.setDepartureDate(busRequest.getDepartureDate());
        busData.setDepartureTime(busRequest.getDepartureTime());
        busData.setSource(busRequest.getSource());
        busData.setDestination(busRequest.getDestination());
        busData.setBusName(busRequest.getBusName());
        busData.setPrice(busRequest.getPrice());
        busData.setNoOfSeatsAvailable(busRequest.getNoOfSeatsAvailable());
        return busDataRepository.save(busData);
    }

    public String deleteBus(int busId) {
        if(busDataRepository.existsById(busId)){
            busDataRepository.deleteById(busId);
            return "Bus removed successfully";
        }
        return "Bus not found";
    }

    public BusData updateBus(BusRequest busRequest, int busId) {
        if(busDataRepository.existsById(busId)){
            BusData busData=busDataRepository.findById(busId).get();
            busData.setDepartureDate(busRequest.getDepartureDate());
            busData.setDepartureTime(busRequest.getDepartureTime());
            busData.setBusName(busRequest.getBusName());
            busData.setSource(busRequest.getSource());
            busData.setDestination(busRequest.getDestination());
            busData.setNoOfSeatsAvailable(busRequest.getNoOfSeatsAvailable());
            busData.setPrice(busRequest.getPrice());
            return busDataRepository.save(busData);
        }else{
            throw new RuntimeException("Bus not found");
        }
    }

    public List<BusData> getBuses() {
        return busDataRepository.findAll();
    }

    public List<CompleteBookingsDTO> getBookings() {
        List<Bookings> bookingsList = bookingsRepository.findAll();
        return bookingsList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CompleteBookingsDTO convertToDTO(Bookings bookings) {
        CompleteBookingsDTO dto = new CompleteBookingsDTO();
        dto.setBookingsId(bookings.getBookingsId());
        dto.setDepartureTime(bookings.getDepartureTime());
        dto.setDepartureDate(bookings.getDepartureDate());
        dto.setNoOfTickets(bookings.getNoOfTickets());
        dto.setTotalCalculated(bookings.getTotalCalculated());
        dto.setSource(bookings.getSource());
        dto.setDestination(bookings.getDestination());
        dto.setBusName(bookings.getBusName());
        dto.setUserId(bookings.getUser().getUserId());  // Retrieve userId
        dto.setBusId(bookings.getBusData().getBusId());  // Retrieve busId
        return dto;
    }
}
