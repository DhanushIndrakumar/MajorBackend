package com.major.majorproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingsId;

    private String departureTime;

    private String departureDate;

    private int noOfTickets ;

    private Double totalCalculated ;

    private String source;

    private String destination;

    private String busName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="busId")
    private BusData busData;

    //private int customerId;


}
