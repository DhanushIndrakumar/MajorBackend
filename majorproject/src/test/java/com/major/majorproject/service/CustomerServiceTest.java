package com.major.majorproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.major.majorproject.DTO.BookingsDTO;
import com.major.majorproject.DTO.ReservationDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.BookingsRepository;
import com.major.majorproject.repositories.BusDataRepository;
import com.major.majorproject.repositories.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CustomerService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CustomerServiceTest {
    @MockBean
    private BookingsRepository bookingsRepository;

    @MockBean
    private BusDataRepository busDataRepository;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testGetAllBuses() {

        ArrayList<BusData> busDataList = new ArrayList<>();
        when(busDataRepository.findBySourceAndDestinationAndDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Date>any())).thenReturn(busDataList);

        List<BusData> actualAllBuses = customerService.getAllBuses(new ReservationDTO());

        verify(busDataRepository).findBySourceAndDestinationAndDate(isNull(), isNull(), isNull());
        assertTrue(actualAllBuses.isEmpty());
        assertSame(busDataList, actualAllBuses);
    }


    @Test
    void testGetAllBuses2() {

        when(busDataRepository.findBySourceAndDestinationAndDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Date>any())).thenThrow(new RuntimeException("foo"));

        assertThrows(RuntimeException.class, () -> customerService.getAllBuses(new ReservationDTO()));
        verify(busDataRepository).findBySourceAndDestinationAndDate(isNull(), isNull(), isNull());
    }


    @Test
    void testBookBus() {
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
        when(bookingsRepository.save(Mockito.<Bookings>any())).thenReturn(bookings);

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Optional<BusData> ofResult = Optional.of(busData2);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(emailService)
                .sendBookingConfirmation(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("password");
        user2.setPhone("6625550144");
        user2.setRole(Role.USER);
        user2.setUserId(1);
        user2.setUserName("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findByUserId(anyInt())).thenReturn(ofResult2);

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


        Bookings actualBookBusResult = customerService.bookBus(bookingsDTO, 1);


        verify(userRepository).findByUserId(eq(1));
        verify(emailService).sendBookingConfirmation(eq("jane.doe@example.org"), eq("Booking Confirmation"), eq(
                "Dear Customer,\n\nYour booking for the bus Bus Name from Source to Destination on 2020-03-01 at Departure Time has been confirmed.\n\nBooking Details:\nNumber of Tickets: 1\nTotal Amount: 10.0\n\nThank you for choosing our service."));
        verify(busDataRepository).findById(eq(1));
        verify(bookingsRepository).save(isA(Bookings.class));
        verify(busDataRepository).save(isA(BusData.class));
        assertSame(bookings, actualBookBusResult);
    }


    @Test
    void testBookBus2() {

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
        when(bookingsRepository.save(Mockito.<Bookings>any())).thenReturn(bookings);

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Optional<BusData> ofResult = Optional.of(busData2);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new RuntimeException("Booking Confirmation")).when(emailService)
                .sendBookingConfirmation(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("password");
        user2.setPhone("6625550144");
        user2.setRole(Role.USER);
        user2.setUserId(1);
        user2.setUserName("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findByUserId(anyInt())).thenReturn(ofResult2);

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

        assertThrows(RuntimeException.class, () -> customerService.bookBus(bookingsDTO, 1));
        verify(userRepository).findByUserId(eq(1));
        verify(emailService).sendBookingConfirmation(eq("jane.doe@example.org"), eq("Booking Confirmation"), eq(
                "Dear Customer,\n\nYour booking for the bus Bus Name from Source to Destination on 2020-03-01 at Departure Time has been confirmed.\n\nBooking Details:\nNumber of Tickets: 1\nTotal Amount: 10.0\n\nThank you for choosing our service."));
        verify(busDataRepository).findById(eq(1));
        verify(bookingsRepository).save(isA(Bookings.class));
        verify(busDataRepository).save(isA(BusData.class));
    }

    @Test
    void testGetBookingsByUserId() {

        ArrayList<Bookings> bookingsList = new ArrayList<>();
        when(bookingsRepository.findBookingsByUserId(anyInt())).thenReturn(bookingsList);

        List<Bookings> actualBookingsByUserId = customerService.getBookingsByUserId(1);

        verify(bookingsRepository).findBookingsByUserId(eq(1));
        assertTrue(actualBookingsByUserId.isEmpty());
        assertSame(bookingsList, actualBookingsByUserId);
    }

    @Test
    void testGetBookingsByUserId2() {

        when(bookingsRepository.findBookingsByUserId(anyInt())).thenThrow(new RuntimeException("foo"));

        assertThrows(RuntimeException.class, () -> customerService.getBookingsByUserId(1));
        verify(bookingsRepository).findBookingsByUserId(eq(1));
    }


    @Test
    void testCancelBookingsByBookingsId() {

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
        Optional<Bookings> ofResult = Optional.of(bookings);
        when(bookingsRepository.existsById(Mockito.<Integer>any())).thenReturn(true);
        doNothing().when(bookingsRepository).deleteById(Mockito.<Integer>any());
        when(bookingsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Optional<BusData> ofResult2 = Optional.of(busData2);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doNothing().when(emailService)
                .sendBookingCancellation(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());


        String actualCancelBookingsByBookingsIdResult = customerService.cancelBookingsByBookingsId(1);


        verify(emailService).sendBookingCancellation(eq("jane.doe@example.org"), eq("Booking Cancellation Confirmation"),
                eq("Dear Customer,\n\nYour booking with ID 1 has been successfully cancelled.\n\nThank you for using our service."));
        verify(bookingsRepository).deleteById(eq(1));
        verify(bookingsRepository).existsById(eq(1));
        verify(bookingsRepository).findById(eq(1));
        verify(busDataRepository).findById(eq(1));
        verify(busDataRepository).save(isA(BusData.class));
        assertEquals("Sorry, not able to cancel the booking.", actualCancelBookingsByBookingsIdResult);
    }


    @Test
    void testCancelBookingsByBookingsId2() {

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
        user.setPassword("iloveyou");
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
        Optional<Bookings> ofResult = Optional.of(bookings);
        doNothing().when(bookingsRepository).deleteById(Mockito.<Integer>any());
        when(bookingsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Optional<BusData> ofResult2 = Optional.of(busData2);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doThrow(new RuntimeException("Booking Cancellation Confirmation")).when(emailService)
                .sendBookingCancellation(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());


        assertThrows(RuntimeException.class, () -> customerService.cancelBookingsByBookingsId(1));
        verify(emailService).sendBookingCancellation(eq("jane.doe@example.org"), eq("Booking Cancellation Confirmation"),
                eq("Dear Customer,\n\nYour booking with ID 1 has been successfully cancelled.\n\nThank you for using our service."));
        verify(bookingsRepository).deleteById(eq(1));
        verify(bookingsRepository).findById(eq(1));
        verify(busDataRepository).findById(eq(1));
        verify(busDataRepository).save(isA(BusData.class));
    }


    @Test
    void testCancelBookingsByBookingsId3() {

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
        user.setPassword("iloveyou");
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
        Optional<Bookings> ofResult = Optional.of(bookings);
        when(bookingsRepository.existsById(Mockito.<Integer>any())).thenReturn(false);
        doNothing().when(bookingsRepository).deleteById(Mockito.<Integer>any());
        when(bookingsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Optional<BusData> ofResult2 = Optional.of(busData2);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doNothing().when(emailService)
                .sendBookingCancellation(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        String actualCancelBookingsByBookingsIdResult = customerService.cancelBookingsByBookingsId(1);

        verify(emailService).sendBookingCancellation(eq("jane.doe@example.org"), eq("Booking Cancellation Confirmation"),
                eq("Dear Customer,\n\nYour booking with ID 1 has been successfully cancelled.\n\nThank you for using our service."));
        verify(bookingsRepository).deleteById(eq(1));
        verify(bookingsRepository).existsById(eq(1));
        verify(bookingsRepository).findById(eq(1));
        verify(busDataRepository).findById(eq(1));
        verify(busDataRepository).save(isA(BusData.class));
        assertEquals("Booking successfully cancelled.", actualCancelBookingsByBookingsIdResult);
    }


    @Test
    void testCancelBookingsByBookingsId4() {

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

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(1);
        busData2.setBusName("Bus Name");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("Departure Time");
        busData2.setDestination("Destination");
        busData2.setNoOfSeatsAvailable(1);
        busData2.setPrice(10.0d);
        busData2.setSource("Source");
        Bookings bookings = mock(Bookings.class);
        when(bookings.getNoOfTickets()).thenThrow(new RuntimeException("Booking Cancellation Confirmation"));
        when(bookings.getBusData()).thenReturn(busData2);
        doNothing().when(bookings).setBookingsId(anyInt());
        doNothing().when(bookings).setBusData(Mockito.<BusData>any());
        doNothing().when(bookings).setBusName(Mockito.<String>any());
        doNothing().when(bookings).setDepartureDate(Mockito.<String>any());
        doNothing().when(bookings).setDepartureTime(Mockito.<String>any());
        doNothing().when(bookings).setDestination(Mockito.<String>any());
        doNothing().when(bookings).setNoOfTickets(anyInt());
        doNothing().when(bookings).setSource(Mockito.<String>any());
        doNothing().when(bookings).setTotalCalculated(Mockito.<Double>any());
        doNothing().when(bookings).setUser(Mockito.<User>any());
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
        Optional<Bookings> ofResult = Optional.of(bookings);
        when(bookingsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        BusData busData3 = new BusData();
        busData3.setBusBookingList(new ArrayList<>());
        busData3.setBusId(1);
        busData3.setBusName("Bus Name");
        busData3.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData3.setDepartureTime("Departure Time");
        busData3.setDestination("Destination");
        busData3.setNoOfSeatsAvailable(1);
        busData3.setPrice(10.0d);
        busData3.setSource("Source");
        Optional<BusData> ofResult2 = Optional.of(busData3);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);


        assertThrows(RuntimeException.class, () -> customerService.cancelBookingsByBookingsId(1));
        verify(bookings).getBusData();
        verify(bookings).getNoOfTickets();
        verify(bookings).setBookingsId(eq(1));
        verify(bookings).setBusData(isA(BusData.class));
        verify(bookings).setBusName(eq("Bus Name"));
        verify(bookings).setDepartureDate(eq("2020-03-01"));
        verify(bookings).setDepartureTime(eq("Departure Time"));
        verify(bookings).setDestination(eq("Destination"));
        verify(bookings).setNoOfTickets(eq(1));
        verify(bookings).setSource(eq("Source"));
        verify(bookings).setTotalCalculated(eq(10.0d));
        verify(bookings).setUser(isA(User.class));
        verify(bookingsRepository).findById(eq(1));
        verify(busDataRepository).findById(eq(1));
    }
}
