package com.example.app.entities;

import com.example.app.entities.enums.PassengerType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(generator = "user_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_sequence_generator", initialValue = 1000, allocationSize = 1)
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    @Enumerated(EnumType.STRING) //save enum as a string
    private PassengerType type;

}
