package com.example.mylittleproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.mylittleproject.model.Teacher;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.TeacherRepo;
import com.example.mylittleproject.service.TeacherService;

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
public class TeacherController {
    
    @Autowired
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    //Get all teachers with an optional teacher lastname search parameter
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Teacher>> getAllTeachers(@RequestParam(required = false) String lastName) {
        log.info("Getting all teachers");
        try{
            List<Teacher> teachers = teacherService.getTeachers(lastName);
            return teachers.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(teachers, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachers/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") long id) {
        log.info("Getting teacher by id " + id);
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        return teacher.isPresent() ? 
            new ResponseEntity<>(teacher.get(), HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Finds a teacher by email
    @GetMapping("teachers/findteacher")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Optional<Teacher>> getTeacherByEmail(@RequestParam (required = true) String email){
        try{
            log.info("Getting teacher by email");
            Optional<Teacher> teacher = teacherService.getTeacherByEmail(email);
            return teacher.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(teacher, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Creates new teacher
    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createTeacher(@RequestBody Teacher teacher) {
        log.info("Creating new teacher");
        try{
            teacherService.createTeacher(teacher);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            log.info("Error creating course: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Assigns a course to a teacher
    @Transactional
    @PutMapping("/teachers/{teacherId}/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addCourseByIdToTeacher(@PathVariable("teacherId") long teacherId,
            @PathVariable("courseId") long courseId) {
        log.info("Adding course to teacher");
        try{
            return (teacherService.addCourseByIdToTeacher(courseId, teacherId)) ?
                new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED) :
                new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            log.info("Error adding course to teacher: " + e.getMessage());
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Updates existing teacher 
    @PutMapping("/teachers/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") long id, @RequestBody Teacher newTeacher) {
        log.info("Updating teacher with id " + id);
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        if(teacher.isPresent()){
            teacher.get().setProfilePic(newTeacher.getProfilePic());
            teacher.get().setFirstName(newTeacher.getFirstName());
            teacher.get().setLastName(newTeacher.getLastName());
            teacher.get().setPhoneNumber(newTeacher.getPhoneNumber());
            teacher.get().setEmail(newTeacher.getEmail());
            teacherService.updateTeacher(id, teacher.get());
        return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTeacherById(@PathVariable("id") long id) {
        log.info("Deleting teacher with id " + id);
        teacherService.deleteTeacher(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @DeleteMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllTeachers() {
        log.info("Deleting all teachers");
        try{
            teacherService.deleteAllTeachers();
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
