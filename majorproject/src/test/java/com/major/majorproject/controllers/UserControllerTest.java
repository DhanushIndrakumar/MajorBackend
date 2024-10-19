package com.major.majorproject.controllers;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.majorproject.DTO.BookingsDTO;
import com.major.majorproject.DTO.ReservationDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.service.CustomerService;

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

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerTest {
    @MockBean
    private CustomerService customerService;

    @Autowired
    private UserController userController;


    @Test
    void testGetAllBuses() throws Exception {

        when(customerService.getAllBuses(Mockito.<ReservationDTO>any())).thenReturn(new ArrayList<>());

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO
                .setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        reservationDTO.setDestination("Destination");
        reservationDTO.setSource("Source");
        String content = (new ObjectMapper()).writeValueAsString(reservationDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/getBuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testBookBus() throws Exception {

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

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");

        Bookings bookings = new Bookings();
        bookings.setBookingsId(1);
        bookings.setBusData(busData);
        bookings.setBusName("Bus Name");
        bookings.setDepartureDate("2020-03-01");
        bookings.setDepartureTime("Departure Time");
        bookings.setDestination("Destination");
        bookings.setNoOfTickets(1);
        bookings.setSource("Source");
        bookings.setTotalCalculated(10.0d);
        bookings.setUser(user);
        when(customerService.bookBus(Mockito.<BookingsDTO>any(), anyInt())).thenReturn(bookings);

        BookingsDTO bookingsDTO = new BookingsDTO();
        bookingsDTO.setBusId(1);
        bookingsDTO.setBusName("Bus Name");
        bookingsDTO.setDepartureDate("2020-03-01");
        bookingsDTO.setDepartureTime("Departure Time");
        bookingsDTO.setDestination("Destination");
        bookingsDTO.setEmail("jane.doe@example.org");
        bookingsDTO.setNoOfTickets(1);
        bookingsDTO.setPrice(10.0d);
        bookingsDTO.setSource("Source");
        bookingsDTO.setTotalCalculated(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(bookingsDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/bookBus/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"bookingsId\":1,\"departureTime\":\"Departure Time\",\"departureDate\":\"2020-03-01\",\"noOfTickets\":1,"
                                        + "\"totalCalculated\":10.0,\"source\":\"Source\",\"destination\":\"Destination\",\"busName\":\"Bus Name\"}"));
    }

    @Test
    void testGetBookingsByUserId() throws Exception {

        when(customerService.getBookingsByUserId(anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/getBookingsByUserId/{userId}",
                1);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testCancelBookingsByBookingsId() throws Exception {

        when(customerService.cancelBookingsByBookingsId(anyInt())).thenReturn("42");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/user/cancelBookingsByBookingsId/{bookingsId}", 1);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("42"));
    }
}
