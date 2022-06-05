package com.example.mylittleproject.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "teacher_id")
    private long id;
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
    @Column(unique = true)
    @NotBlank(message = "Email cannot be empty")
    private String email;
    private String phoneNumber;
    private String profilePic;

    //@JsonIgnore
    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Teacher(String firstName, String lastName, String email, String phoneNumber, String profilePic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
    }


}
