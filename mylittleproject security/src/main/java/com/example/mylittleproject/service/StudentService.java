package com.example.mylittleproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Event;
import com.example.mylittleproject.model.Student;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.StudentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService {
    private CourseRepo courseRepo;
    private StudentRepo studentRepo;

    @Autowired
    public StudentService(CourseRepo courseRepo, StudentRepo studentRepo) {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
    }

    public List<Student> getStudents(String studentName){
        return (studentName == null) ? 
            studentRepo.findAll() : 
            studentRepo.findByLastNameContaining(studentName);
    }

    public Optional<Student> getStudentById(long id){
        return studentRepo.findById(id);
    }

    public Optional<Student> getStudentByEmail(String email){
        return studentRepo.findByEmail(email);
    }

    //Get all courses a student is enrolled in
    public List<Course> getCoursesByStudentId(long id){
        return getStudentById(id).get().getCourses();
    }

    public List<Event> getEventsByStudentId(long id){
        List<Event> events = new ArrayList<>();
        //add each event from each course to student events and return 
        studentRepo.getById(id).getCourses().forEach((c) -> events.addAll(c.getEvents()));
        return events;
    }


    public boolean addCourseByIdToStudent(long courseId, long studentId){
        if(!courseRepo.existsById(studentId))
            return false;
        //adds a course to student.getCourses and persists
        Student s = studentRepo.getById(studentId);
        s.getCourses().add(courseRepo.getById(courseId));
        studentRepo.save(s);
        
        // Course c = courseRepo.getById(courseId);
        // c.getStudents().add(studentRepo.getById(studentId));
        // courseRepo.save(c);
        return true;
    }

    //Unenrolls a student from a course
    public boolean deleteCourseByIdFromStudent(long courseId, long studentId){
        if(!courseRepo.existsById(studentId))
            return false;
        Student s = studentRepo.getById(studentId);
        s.getCourses().remove(courseRepo.getById(courseId));
        studentRepo.save(s);
        
        // Course c = courseRepo.getById(courseId);
        // c.getStudents().add(studentRepo.getById(studentId));
        // courseRepo.save(c);
        return true;
    }

    public Student createStudent(Student student){
        return studentRepo.save(student);
    }

    public Student updateStudent(long id, Student newStudent){
        if(studentRepo.existsById(id)){
            newStudent.setId(id);
            return studentRepo.save(newStudent);
        }
        return null;
    }

    public void deleteStudent(long id){
        studentRepo.deleteById(id);
    }

    public void deleteAllStudents(){
        studentRepo.deleteAll();
    }
}
