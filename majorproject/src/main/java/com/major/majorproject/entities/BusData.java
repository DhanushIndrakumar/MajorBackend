package com.major.majorproject.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="BusData")
public class BusData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int busId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date departureDate;

    private String departureTime;

    private String source;

    private String destination;

    private Double price;

    private String busName;

    private int noOfSeatsAvailable;

    @JsonIgnore
    @OneToMany(mappedBy= "busData",cascade=CascadeType.ALL)
    private List<Bookings> busBookingList;//you can use new ArrayList for it to display

}
