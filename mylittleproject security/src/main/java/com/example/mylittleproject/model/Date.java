package com.example.mylittleproject.model;

import java.sql.Time;
import java.time.DayOfWeek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "dates")
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private long id;
    @NotBlank
    private DayOfWeek dow;
    @NotBlank
    private Time startTime;
    @NotBlank
    private Time endTime;

    public Date(DayOfWeek dow, Time startTime, Time endTime) {
        this.dow = dow;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
