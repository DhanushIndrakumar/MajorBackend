package com.major.majorproject.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingsDTO {

    @NotBlank(message = "Departure date cannot be blank")
    private String departureDate;

    @NotBlank(message = "Departure time cannot be blank")
    private String departureTime;

    @NotBlank(message = "Source cannot be blank")
    private String source;

    @NotBlank(message = "Destination cannot be blank")
    private String destination;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotBlank(message = "Bus name cannot be blank")
    private String busName;

    @Min(value = 1, message = "Number of tickets must be at least 1")
    private int noOfTickets;

    @NotNull(message = "Total calculated cannot be null")
    @Positive(message = "Total calculated must be a positive number")
    private Double totalCalculated;

    @NotNull(message = "Bus ID cannot be null")
    private int busId;

    private String email;


}
