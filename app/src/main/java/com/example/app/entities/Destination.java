package com.example.app.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="destination")
public class Destination {
    @Id
    @GeneratedValue(generator = "destination_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "destination_sequence_generator", initialValue = 1000, allocationSize = 1)
    private Long id;
    private String name;
}