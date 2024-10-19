package com.major.majorproject.controllers;


import com.major.majorproject.DTO.BusRequest;
import com.major.majorproject.DTO.CompleteBookingsDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(
            description = "End point to add a bus data",
            summary="Admin can add Bus data by providing the bus details"
    )
    @PostMapping("/addBus")
    public BusData addBus(@Valid @RequestBody BusRequest busRequest){
        return adminService.addBus(busRequest);
    }

    @Operation(
            description = "End point remove a bus data",
            summary="Admin can remove bus by providing the valid busId"
    )
    @DeleteMapping("/deleteBus/{busId}")
    public String deleteBus(@PathVariable int busId){
        return adminService.deleteBus(busId);
    }

    @Operation(
            description = "End point edit bus data",
            summary="Admin can edit the bus data by providing the valid busId"
    )
    @PutMapping("/updateBus/{busId}")
    public BusData updateBus(@RequestBody BusRequest busRequest,@PathVariable int busId){
        return adminService.updateBus(busRequest,busId);
    }


    @Operation(
            description = "End point retrieve all the bus data",
            summary="Admin can edit the bus data by providing the valid busId"
    )
    @GetMapping("/getBuses")
    public List<BusData> getBuses(){
        return adminService.getBuses();
    }

    @Operation(
            description = "End point retrieve all the bookings",
            summary="Admin can retrieve all the bookings done by the customers"
    )
    @GetMapping("/getBookings")
    public List<CompleteBookingsDTO> getBookings(){
        return adminService.getBookings();
    }


}
