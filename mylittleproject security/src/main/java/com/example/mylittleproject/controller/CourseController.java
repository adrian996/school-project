package com.example.mylittleproject.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Student;
import com.example.mylittleproject.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin("http://localhost:4200")
public class CourseController {
    private CourseService courseService;
    
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    //Returns all courses with an optional course name search parameter
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getCourses(@RequestParam(required = false) String courseName){
        try{
            log.info("Getting all courses");
            List<Course> courses = courseService.getCourses(courseName);
            return courses.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(courses, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Returns course by id
    @GetMapping("/courses/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id){
        log.info("Retrieving course with id:" + id);
        Optional<Course> course = courseService.getCourseById(id);
        return course.isPresent() ? 
            new ResponseEntity<>(course.get(), HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //Returns students enrolled in a course
    @GetMapping("/courses/{id}/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<Student>> getStudentsByCourseId(@PathVariable("id") long id){
        log.info("Retreving students from course id " + id);
        return new ResponseEntity<>(courseService.getStudentsByCourseId(id), HttpStatus.OK);
    }

    @PutMapping("/courses/addstudent/{courseId}/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addStudentByIdToCourse(@PathVariable("studentId") long studentId,
        @PathVariable("courseId") long courseId){
            log.info("Adding student with id " + studentId + " to course with id " + courseId);
            try{
                return (courseService.addStudentByIdToCourse(studentId, courseId)) ?
                    new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED) :
                    new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
            }catch(Exception e){
                log.info("Error adding student to course: " + e.getMessage());
                return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    //Create a new course
    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createCourse(@RequestBody Course course){
        log.info("Creating new course");
        try{
            courseService.createCourse(course);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            log.info("Error creating course: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update existing course
    @PutMapping("/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long id, @RequestBody Course newCourse){
        log.info("Updating course with id " + id);
        Optional<Course> course = courseService.getCourseById(id);
        if(course.isPresent()){
            //This maintains entries in join table
            course.get().setName(newCourse.getName());
            course.get().setDescription(newCourse.getDescription());
            course.get().setPoints(newCourse.getPoints());
            courseService.updateCourse(id, course.get());
            return new ResponseEntity<>(course.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    //Assign a teacher to a course
    @Transactional
    @PutMapping("/courses/addteacher/{courseId}/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addTeacherByIdToCourse(@PathVariable("teacherId") long teacherId,
            @PathVariable("courseId") long courseId) {
        log.info("Setting teacher with id " + teacherId + "to course with id " + courseId);
        try{
            return (courseService.addTeacherByIdToCourse(teacherId, courseId)) ?
                new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED) :
                new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            log.info("Error adding student to course: " + e.getMessage());
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete a course
    @DeleteMapping("/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id){
        log.info("Deleting course with id " + id);
        courseService.deleteCourse(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    //Deletes all courses and cascades changes to join tables.
    @DeleteMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllCourses(){
        log.info("Deleting all courses");
        try{
            courseService.deleteAllCourses();
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
