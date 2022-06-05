package com.example.mylittleproject.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private long id;
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String profilePic;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        // uniqueConstraints = {
        //     @UniqueConstraint(columnNames = {"student_id", "course_id"})
        // },
        name = "studcourses",
        joinColumns = {@JoinColumn(name = "student_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")})
    @Valid
    private List<Course> courses = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Student(String firstName, String lastName, String email, String phoneNumber, String profilePic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic; //new RestTemplate().getForObject("https://fakeface.rest/face/json?gender=male&minimum_age=20&maximum_age=30", String.class);
    }

    

}
