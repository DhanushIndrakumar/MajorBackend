package com.major.majorproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.major.majorproject.DTO.BusRequest;
import com.major.majorproject.DTO.CompleteBookingsDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.BookingsRepository;
import com.major.majorproject.repositories.BusDataRepository;

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

@ContextConfiguration(classes = {AdminService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @MockBean
    private BookingsRepository bookingsRepository;

    @MockBean
    private BusDataRepository busDataRepository;


    @Test
    void testAddBus() {
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
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");

        BusData actualAddBusResult = adminService.addBus(busRequest);

        verify(busDataRepository).save(isA(BusData.class));
        assertSame(busData, actualAddBusResult);
    }


    @Test
    void testAddBus2() {

        when(busDataRepository.save(Mockito.<BusData>any())).thenThrow(new RuntimeException("foo"));

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");


        assertThrows(RuntimeException.class, () -> adminService.addBus(busRequest));
        verify(busDataRepository).save(isA(BusData.class));
    }


    @Test
    void testDeleteBus() {

        doNothing().when(busDataRepository).deleteById(Mockito.<Integer>any());
        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(true);

        String actualDeleteBusResult = adminService.deleteBus(1);

        verify(busDataRepository).deleteById(eq(1));
        verify(busDataRepository).existsById(eq(1));
        assertEquals("Bus removed successfully", actualDeleteBusResult);
    }

    @Test
    void testDeleteBus2() {
        doThrow(new RuntimeException("Bus removed successfully")).when(busDataRepository)
                .deleteById(Mockito.<Integer>any());
        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> adminService.deleteBus(1));
        verify(busDataRepository).deleteById(eq(1));
        verify(busDataRepository).existsById(eq(1));
    }


    @Test
    void testDeleteBus3() {

        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(false);

        String actualDeleteBusResult = adminService.deleteBus(1);

        verify(busDataRepository).existsById(eq(1));
        assertEquals("Bus not found", actualDeleteBusResult);
    }


    @Test
    void testUpdateBus() {
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
        Optional<BusData> ofResult = Optional.of(busData);

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
        when(busDataRepository.save(Mockito.<BusData>any())).thenReturn(busData2);
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(true);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");


        BusData actualUpdateBusResult = adminService.updateBus(busRequest, 1);

        verify(busDataRepository).existsById(eq(1));
        verify(busDataRepository).findById(eq(1));
        verify(busDataRepository).save(isA(BusData.class));
        assertSame(busData2, actualUpdateBusResult);
    }


    @Test
    void testUpdateBus2() {

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
        Optional<BusData> ofResult = Optional.of(busData);
        when(busDataRepository.save(Mockito.<BusData>any())).thenThrow(new RuntimeException("foo"));
        when(busDataRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(true);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");


        assertThrows(RuntimeException.class, () -> adminService.updateBus(busRequest, 1));
        verify(busDataRepository).existsById(eq(1));
        verify(busDataRepository).findById(eq(1));
        verify(busDataRepository).save(isA(BusData.class));
    }

    /**
     * Method under test: {@link AdminService#updateBus(BusRequest, int)}
     */
    @Test
    void testUpdateBus3() {

        when(busDataRepository.existsById(Mockito.<Integer>any())).thenReturn(false);

        BusRequest busRequest = new BusRequest();
        busRequest.setBusName("Bus Name");
        busRequest.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busRequest.setDepartureTime("Departure Time");
        busRequest.setDestination("Destination");
        busRequest.setNoOfSeatsAvailable(1);
        busRequest.setPrice(10.0d);
        busRequest.setSource("Source");


        assertThrows(RuntimeException.class, () -> adminService.updateBus(busRequest, 1));
        verify(busDataRepository).existsById(eq(1));
    }


    @Test
    void testGetBuses() {

        ArrayList<BusData> busDataList = new ArrayList<>();
        when(busDataRepository.findAll()).thenReturn(busDataList);


        List<BusData> actualBuses = adminService.getBuses();
        verify(busDataRepository).findAll();
        assertTrue(actualBuses.isEmpty());
        assertSame(busDataList, actualBuses);
    }


    @Test
    void testGetBuses2() {

        when(busDataRepository.findAll()).thenThrow(new RuntimeException("foo"));

        assertThrows(RuntimeException.class, () -> adminService.getBuses());
        verify(busDataRepository).findAll();
    }


    @Test
    void testGetBookings() {

        when(bookingsRepository.findAll()).thenReturn(new ArrayList<>());

        List<CompleteBookingsDTO> actualBookings = adminService.getBookings();

        verify(bookingsRepository).findAll();
        assertTrue(actualBookings.isEmpty());
    }


    @Test
    void testGetBookings2() {

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

        ArrayList<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(bookings);
        when(bookingsRepository.findAll()).thenReturn(bookingsList);

        // Act
        List<CompleteBookingsDTO> actualBookings = adminService.getBookings();

        // Assert
        verify(bookingsRepository).findAll();
        assertEquals(1, actualBookings.size());
        CompleteBookingsDTO getResult = actualBookings.get(0);
        assertEquals("2020-03-01", getResult.getDepartureDate());
        assertEquals("Bus Name", getResult.getBusName());
        assertEquals("Departure Time", getResult.getDepartureTime());
        assertEquals("Destination", getResult.getDestination());
        assertEquals("Source", getResult.getSource());
        assertEquals(1, getResult.getBookingsId());
        assertEquals(1, getResult.getBusId());
        assertEquals(1, getResult.getNoOfTickets());
        assertEquals(1, getResult.getUserId());
        assertEquals(10.0d, getResult.getTotalCalculated().doubleValue());
    }


    @Test
    void testGetBookings3() {

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

        BusData busData2 = new BusData();
        busData2.setBusBookingList(new ArrayList<>());
        busData2.setBusId(2);
        busData2.setBusName("Rajahamsa");
        busData2.setDepartureDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        busData2.setDepartureTime("17:00");
        busData2.setDestination("Chennai");
        busData2.setNoOfSeatsAvailable(0);
        busData2.setPrice(0.5d);
        busData2.setSource("Bangalore");

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("john.smith@example.org");
        user2.setPassword("Password");
        user2.setPhone("8605550118");
        user2.setRole(Role.ADMIN);
        user2.setUserId(2);
        user2.setUserName("User Name");

        Bookings bookings2 = new Bookings();
        bookings2.setBookingsId(2);
        bookings2.setBusData(busData2);
        bookings2.setBusName("Rajahamsa");
        bookings2.setDepartureDate("2020/03/01");
        bookings2.setDepartureTime("17:00");
        bookings2.setDestination("Chennai");
        bookings2.setNoOfTickets(0);
        bookings2.setSource("Bangalore");
        bookings2.setTotalCalculated(0.5d);
        bookings2.setUser(user2);

        ArrayList<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(bookings2);
        bookingsList.add(bookings);
        when(bookingsRepository.findAll()).thenReturn(bookingsList);

        // Act
        List<CompleteBookingsDTO> actualBookings = adminService.getBookings();

        // Assert
        verify(bookingsRepository).findAll();
        assertEquals(2, actualBookings.size());
        CompleteBookingsDTO getResult = actualBookings.get(1);
        assertEquals("2020-03-01", getResult.getDepartureDate());
        CompleteBookingsDTO getResult2 = actualBookings.get(0);
        assertEquals("2020/03/01", getResult2.getDepartureDate());
        assertEquals("Bus Name", getResult.getBusName());
        assertEquals("Departure Time", getResult.getDepartureTime());
        assertEquals("Destination", getResult.getDestination());
        assertEquals("Source", getResult.getSource());
        assertEquals("Rajahamsa", getResult2.getBusName());
        assertEquals("17:00", getResult2.getDepartureTime());
        assertEquals("Chennai", getResult2.getDestination());
        assertEquals("Bangalore", getResult2.getSource());
        assertEquals(0, getResult2.getNoOfTickets());
        assertEquals(0.5d, getResult2.getTotalCalculated().doubleValue());
        assertEquals(1, getResult.getBookingsId());
        assertEquals(1, getResult.getBusId());
        assertEquals(1, getResult.getNoOfTickets());
        assertEquals(1, getResult.getUserId());
        assertEquals(10.0d, getResult.getTotalCalculated().doubleValue());
        assertEquals(2, getResult2.getBookingsId());
        assertEquals(2, getResult2.getBusId());
        assertEquals(2, getResult2.getUserId());
    }


    @Test
    void testGetBookings4() {

        when(bookingsRepository.findAll()).thenThrow(new RuntimeException("foo"));

        assertThrows(RuntimeException.class, () -> adminService.getBookings());
        verify(bookingsRepository).findAll();
    }


    @Test
    void testGetBookings5() {

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

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("password");
        user2.setPhone("6625550144");
        user2.setRole(Role.USER);
        user2.setUserId(1);
        user2.setUserName("janedoe");
        Bookings bookings = mock(Bookings.class);
        when(bookings.getBusData()).thenThrow(new RuntimeException("foo"));
        when(bookings.getUser()).thenReturn(user2);
        when(bookings.getBookingsId()).thenReturn(1);
        when(bookings.getNoOfTickets()).thenReturn(1);
        when(bookings.getTotalCalculated()).thenReturn(10.0d);
        when(bookings.getBusName()).thenReturn("Bus Name");
        when(bookings.getDepartureDate()).thenReturn("2020-03-01");
        when(bookings.getDepartureTime()).thenReturn("Departure Time");
        when(bookings.getDestination()).thenReturn("Destination");
        when(bookings.getSource()).thenReturn("Source");
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

        ArrayList<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(bookings);
        when(bookingsRepository.findAll()).thenReturn(bookingsList);

        assertThrows(RuntimeException.class, () -> adminService.getBookings());
        verify(bookings).getBookingsId();
        verify(bookings).getBusData();
        verify(bookings).getBusName();
        verify(bookings).getDepartureDate();
        verify(bookings).getDepartureTime();
        verify(bookings).getDestination();
        verify(bookings).getNoOfTickets();
        verify(bookings).getSource();
        verify(bookings).getTotalCalculated();
        verify(bookings).getUser();
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
        verify(bookingsRepository).findAll();
    }
}
