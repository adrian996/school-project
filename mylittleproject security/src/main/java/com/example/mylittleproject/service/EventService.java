package com.example.mylittleproject.service;

import java.util.List;

import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Event;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.EventRepo;
import com.example.mylittleproject.repo.StudentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private EventRepo eventRepo;
    private CourseRepo courseRepo;
    
    @Autowired
    public EventService(EventRepo eventRepo, CourseRepo courseRepo){
        this.eventRepo = eventRepo;
        this.courseRepo = courseRepo;
    }

    public List<Event> getEvents(){
        return eventRepo.findAll();
    }

    public List<Event> getByCourseId(long id){
        return eventRepo.findByCourseId(id);
    }

    public boolean createEventForCourse(long courseId, Event event){
        if(!courseRepo.existsById(courseId))
            return false;
        //adding event to course.events collection
        Course c = courseRepo.getById(courseId);
        c.getEvents().add(event);
        courseRepo.save(c);
        return true;
    }

    public Event createEvent(Event event){
        return eventRepo.save(event);
    }

    public void deleteEvent(long id){
        eventRepo.deleteById(id);
    }
}
