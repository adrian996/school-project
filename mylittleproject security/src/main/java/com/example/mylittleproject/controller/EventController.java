package com.example.mylittleproject.controller;

import java.util.List;
import java.util.Set;

import com.example.mylittleproject.model.Event;
import com.example.mylittleproject.service.EventService;
import com.example.mylittleproject.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin("http://localhost:4200")
public class EventController {
    @Autowired
    private final EventService eventService;
    @Autowired
    private final StudentService studentService;

    public EventController(EventService eventService, StudentService studentService) {
        this.eventService = eventService;
        this.studentService = studentService;
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEvents(){
        try{
            log.info("Getting all events");
            List<Event> events = eventService.getEvents();
            return events.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(events, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get list of events for every course
    @GetMapping("/events/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByCourseId(@PathVariable("id") long id){
        log.info("Retrieving event with courseId:" + id);
        List<Event> events = eventService.getByCourseId(id);
        return (!events.isEmpty()) ? 
            new ResponseEntity<>(events, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Get list of students for every event.
    @GetMapping("/events/{id}/students")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByStudentId(@PathVariable("id") long id){
        log.info("Retrieving event with studentId:" + id);
        List<Event> events = studentService.getEventsByStudentId(id);
        return (!events.isEmpty()) ? 
            new ResponseEntity<>(events, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Create a new course
    @PostMapping("/events/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<HttpStatus> createEventForCourse(@PathVariable("courseId") long courseId, @RequestBody Event event){
        log.info("Creating new event for course id" + courseId);
        try{
            eventService.createEventForCourse(courseId, event);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            log.info("Error creating event: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Create a new event
    @PostMapping("/events")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<HttpStatus> createEvent(@RequestBody Event event){
        log.info("Creating new event");
        try{
            eventService.createEvent(event);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            log.info("Error creating new event: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/events/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id){
        log.info("Deleting event with id " + id);
        eventService.deleteEvent(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }


}
