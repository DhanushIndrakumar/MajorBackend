package com.major.majorproject.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

@Data
public class BusRequest {

    @NotNull(message = "Departure date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date departureDate;

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

    @Min(value = 1, message = "Number of seats available must be at least 1")
    private int noOfSeatsAvailable;
}
