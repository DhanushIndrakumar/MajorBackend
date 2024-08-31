package com.major.majorproject.service;


import com.major.majorproject.DTO.BusRequest;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.repositories.BookingsRepository;
import com.major.majorproject.repositories.BusDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Bookings> getBookings() {
        return bookingsRepository.findAll();
    }
}
