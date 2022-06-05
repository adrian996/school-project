package com.example.mylittleproject.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Event;
import com.example.mylittleproject.model.Student;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.StudentRepo;
import com.example.mylittleproject.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin("http://localhost:4200")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    //Returns all students with an optional student lastname search parameter
    @GetMapping("/students")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam (required = false) String studentName){
        try{
            log.info("Getting all students");
            List<Student> students = studentService.getStudents(studentName);
            return students.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(students, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Finds a student by email
    @GetMapping("students/findstudent")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Optional<Student>> getStudentByEmail(@RequestParam (required = true) String email){
        try{
            log.info("Getting student by email");
            Optional<Student> student = studentService.getStudentByEmail(email);
            return student.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(student, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") long id){
        log.info("Retrieving student with id:" + id);
        Optional<Student> student = studentService.getStudentById(id);
        return student.isPresent() ? 
            new ResponseEntity<>(student.get(), HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Get all courses a student is enrolled in
    @GetMapping("/students/{id}/courses")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable("id") long id){
        log.info("Retreving courses from student id " + id);
        return new ResponseEntity<>(studentService.getCoursesByStudentId(id), HttpStatus.OK);
    }

    //Get list of students for every event. Accesses student.getCourses.GetEvents.
    @GetMapping("/students/{id}/events")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Event>> getEventsByStudentId(@PathVariable("id") long id){
        log.info("Retrieving events from student id " + id);
        return new ResponseEntity<>(studentService.getEventsByStudentId(id), HttpStatus.OK);
    }

    @PutMapping("/students/{studentId}/{courseId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addCourseByIdToStudent(@PathVariable("studentId") long studentId,
        @PathVariable("courseId") long courseId){
        log.info("Adding course with id " + courseId + " to student with id " + studentId);
        try{
            return (studentService.addCourseByIdToStudent(courseId, studentId)) ?
                new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED) :
                new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            log.info("Error adding course to student: " + e.getMessage());
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Deletes a course from student, and cascades to join table
    @DeleteMapping("/students/{studentId}/{courseId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteCourseByIdFromStudent(@PathVariable("studentId") long studentId,
        @PathVariable("courseId") long courseId){
        log.info("Deleting course with id " + courseId + " from student with id " + studentId);
        try{
            return (studentService.deleteCourseByIdFromStudent(courseId, studentId)) ?
                new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED) :
                new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            log.info("Error deleting course to student: " + e.getMessage());
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createStudent(@RequestBody Student student){
        log.info("Creating new student");
        try{
            studentService.createStudent(student);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            log.info("Error creating student: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update an existing student
    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") long id, @RequestBody Student newStudent){
        log.info("Updating student with id " + id);
        Optional<Student> student = studentService.getStudentById(id);
        if(student.isPresent()){
            //This maintains entries in join table
            student.get().setFirstName(newStudent.getFirstName());
            student.get().setLastName(newStudent.getLastName());
            student.get().setPhoneNumber(newStudent.getPhoneNumber());
            student.get().setEmail(newStudent.getEmail());
            studentService.updateStudent(id, student.get());
            return new ResponseEntity<>(student.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") long id){
        log.info("Deleting student with id " + id);
        studentService.deleteStudent(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @DeleteMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllStudents(){
        log.info("Deleting all students");
        try{
            studentService.deleteAllStudents();
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
