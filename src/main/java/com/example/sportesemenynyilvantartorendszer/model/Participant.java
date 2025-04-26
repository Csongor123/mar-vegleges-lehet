package com.example.sportesemenynyilvantartorendszer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String email;
    private LocalDate activityDate;


    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;
}
