package com.major.majorproject.controllers;


import com.major.majorproject.DTO.BookingsDTO;
import com.major.majorproject.DTO.ReservationDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Validated
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Operation(
            description = "End point to find bus data",
            summary="Customer can find the list of buses by providing the source,destination and date of travel"
    )
    @PostMapping("/getBuses")
    public List<BusData> getAllBuses(@Valid @RequestBody ReservationDTO reservationDTO){
        return customerService.getAllBuses(reservationDTO);
    }


    @Operation(
            description = "End point to book the bus tickets",
            summary="Customer can book the bus tickets by providing number of tickets required "
    )
    @PostMapping("/bookBus/{userId}")
    public Bookings bookBus(@RequestBody BookingsDTO bookingsDTO, @PathVariable int userId){
        return customerService.bookBus(bookingsDTO,userId);

    }

    @Operation(
            description = "End point to get all the bookings made by the customer",
            summary="Customer can get all the bookings made by them by providing the userId "
    )
    @GetMapping("/getBookingsByUserId/{userId}")
    public List<Bookings> getBookingsByUserId(@PathVariable int userId){
        return customerService.getBookingsByUserId(userId);
    }

    @Operation(
            description = "End point to cancel the booking made by the customer by providing the bookings Id",
            summary="Customer can cancel the booking made by them "
    )
    @DeleteMapping("/cancelBookingsByBookingsId/{bookingsId}")
    public String cancelBookingsByBookingsId(@PathVariable int bookingsId){
        return customerService.cancelBookingsByBookingsId(bookingsId);
    }


}
