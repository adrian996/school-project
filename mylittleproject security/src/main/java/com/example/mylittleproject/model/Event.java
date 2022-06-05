package com.example.mylittleproject.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events")
@JsonIgnoreProperties({"course_id", "date_id", "venue_id"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    // @OneToOne
    // @JoinColumn(name = "teacher_id")
    // private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "date_id")
    private Date date;
    //private String time;
    private String venue;
}
