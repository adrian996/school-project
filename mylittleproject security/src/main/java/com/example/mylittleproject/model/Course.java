package com.example.mylittleproject.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private long id;
    @Column(unique = true)
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private String description;
    private int points;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        // uniqueConstraints = {
        //     @UniqueConstraint(columnNames = {"student_id", "course_id"})
        // },
        name = "studcourses",
        joinColumns = {@JoinColumn(name = "course_id")},
        inverseJoinColumns = {@JoinColumn(name = "student_id")})
    @Valid
    private Set<Student> students = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    //@JoinColumn(name = "event_id")
    private List<Event> events;

    public Course(String name, String description, int points) {
        this.name = name;
        this.description = description;
        this.points = points;
    }

}