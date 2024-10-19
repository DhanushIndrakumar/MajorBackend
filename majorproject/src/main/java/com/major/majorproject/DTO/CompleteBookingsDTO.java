package com.major.majorproject.DTO;

import lombok.Data;

@Data
public class CompleteBookingsDTO {
    private int bookingsId;
    private String departureTime;
    private String departureDate;
    private int noOfTickets;
    private Double totalCalculated;
    private String source;
    private String destination;
    private String busName;
    private int userId;
    private int busId;
}
