package com.major.majorproject.controllers;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.majorproject.DTO.BusRequest;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.service.AdminService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AdminController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AdminControllerTest {
    @Autowired
    private AdminController adminController;

    @MockBean
    private AdminService adminService;


    @Test
    void testAddBus() throws Exception {

        BusData busData = new BusData();
        busData.setBusBookingList(new ArrayList<>());
        busData.setBusId(1);
        busData.setBusName("Bus Name");
        busData.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData.setDepartureTime("Departure Time");
        busData.setDestination("Destination");
        busData.setNoOfSeatsAvailable(1);
        busData.setPrice(10.0d);
        busData.setSource("Source");
        when(adminService.addBus(Mockito.<BusRequest>any())).thenReturn(busData);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");
        String content = (new ObjectMapper()).writeValueAsString(busRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/admin/addBus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);


        MockMvcBuilders.standaloneSetup(adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"busId\":1,\"departureDate\":\"01-01-1970\",\"departureTime\":\"Departure Time\",\"source\":\"Source\",\"destination"
                                        + "\":\"Destination\",\"price\":10.0,\"busName\":\"Bus Name\",\"noOfSeatsAvailable\":1}"));
    }


    @Test
    void testDeleteBus() throws Exception {
        // Arrange
        when(adminService.deleteBus(anyInt())).thenReturn("Delete Bus");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/admin/deleteBus/{busId}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete Bus"));
    }


    @Test
    void testUpdateBus() throws Exception {
        // Arrange
        BusData busData = new BusData();
        busData.setBusBookingList(new ArrayList<>());
        busData.setBusId(1);
        busData.setBusName("Bus Name");
        busData.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData.setDepartureTime("Departure Time");
        busData.setDestination("Destination");
        busData.setNoOfSeatsAvailable(1);
        busData.setPrice(10.0d);
        busData.setSource("Source");
        when(adminService.updateBus(Mockito.<BusRequest>any(), anyInt())).thenReturn(busData);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");
        String content = (new ObjectMapper()).writeValueAsString(busRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/admin/updateBus/{busId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"busId\":1,\"departureDate\":\"01-01-1970\",\"departureTime\":\"Departure Time\",\"source\":\"Source\",\"destination"
                                        + "\":\"Destination\",\"price\":10.0,\"busName\":\"Bus Name\",\"noOfSeatsAvailable\":1}"));
    }


    @Test
    void testGetBuses() throws Exception {

        when(adminService.getBuses()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/admin/getBuses");

        MockMvcBuilders.standaloneSetup(adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetBookings() throws Exception {

        when(adminService.getBookings()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/admin/getBookings");

        MockMvcBuilders.standaloneSetup(adminController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
