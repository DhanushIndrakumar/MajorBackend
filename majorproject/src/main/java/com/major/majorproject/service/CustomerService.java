package com.major.majorproject.service;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.major.majorproject.DTO.BookingsDTO;
import com.major.majorproject.DTO.ReservationDTO;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.BusData;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.BookingsRepository;
import com.major.majorproject.repositories.BusDataRepository;
import com.major.majorproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private BusDataRepository busDataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private EmailService emailService;

    public List<BusData> getAllBuses(ReservationDTO reservationDTO) {
        return busDataRepository.findBySourceAndDestinationAndDate(reservationDTO.getSource(),reservationDTO.getDestination(),reservationDTO.getDepartureDate());

    }
    public Bookings bookBus(BookingsDTO bookingsDTO, int userId) {
        // Create a new Bookings instance
        Bookings bookings = new Bookings();

        BusData busData = busDataRepository.findById(bookingsDTO.getBusId()).get();

        if (bookingsDTO.getNoOfTickets() <= busData.getNoOfSeatsAvailable() && bookingsDTO.getNoOfTickets() > 0) {
            // Set properties from bookingsDTO
            int remSeats = busData.getNoOfSeatsAvailable() - bookingsDTO.getNoOfTickets();
            busData.setNoOfSeatsAvailable(remSeats);
            busDataRepository.save(busData);
            bookings.setSource(bookingsDTO.getSource());
            bookings.setDestination(bookingsDTO.getDestination());
            bookings.setDepartureDate(bookingsDTO.getDepartureDate());
            bookings.setDepartureTime(bookingsDTO.getDepartureTime());
            bookings.setNoOfTickets(bookingsDTO.getNoOfTickets());
            bookings.setBusName(bookingsDTO.getBusName());
            bookings.setTotalCalculated(bookingsDTO.getTotalCalculated());
            bookings.setBusData(busData);

            // Fetch user from the repository
            Optional<User> optionalUser = userRepository.findByUserId(userId);

            if (optionalUser.isPresent()) {
                // Set the user in the bookings
                bookings.setUser(optionalUser.get());
            } else {
                // Handle the case where the user is not found
                throw new RuntimeException("User with ID " + userId + " not found.");
            }

            // Save the bookings object
            Bookings savedBooking = bookingsRepository.save(bookings);

            // Prepare email content
            String subject = "Booking Confirmation";
            String body = "Dear Customer,\n\nYour booking for the bus " + savedBooking.getBusName() +
                    " from " + savedBooking.getSource() + " to " + savedBooking.getDestination() +
                    " on " + savedBooking.getDepartureDate() + " at " + savedBooking.getDepartureTime() +
                    " has been confirmed.\n\nBooking Details:\n" +
                    "Number of Tickets: " + savedBooking.getNoOfTickets() + "\n" +
                    "Total Amount: " + savedBooking.getTotalCalculated() + "\n\nThank you for choosing our service.";

            // Send confirmation email
            emailService.sendBookingConfirmation(bookingsDTO.getEmail(), subject, body);

            return savedBooking;

        } else {
            throw new RuntimeException("Error: Insufficient seats available or invalid number of tickets.");
        }
    }

    public List<Bookings> getBookingsByUserId(int userId) {

        return bookingsRepository.findBookingsByUserId(userId);
    }

    public String cancelBookingsByBookingsId(int bookingsId){
        Bookings bookings = bookingsRepository.findById(bookingsId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        BusData busData = busDataRepository.findById(bookings.getBusData().getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        // Increase available seats
        int finalSeats = busData.getNoOfSeatsAvailable() + bookings.getNoOfTickets();
        busData.setNoOfSeatsAvailable(finalSeats);
        busDataRepository.save(busData);
        bookingsRepository.deleteById(bookingsId);

        // Prepare cancellation email content
        String subject = "Booking Cancellation Confirmation";
        String body = "Dear Customer,\n\nYour booking with ID " + bookingsId +
                " has been successfully cancelled.\n\nThank you for using our service.";

        // Send cancellation email
        emailService.sendBookingCancellation(bookings.getUser().getEmail(), subject, body);

        if(bookingsRepository.existsById(bookingsId)){
            return "Sorry, not able to cancel the booking.";
        } else {
            return "Booking successfully cancelled.";
        }
    }

}

