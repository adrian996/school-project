package com.example.mylittleproject.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Student;
import com.example.mylittleproject.model.Teacher;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.EventRepo;
import com.example.mylittleproject.repo.StudentRepo;
import com.example.mylittleproject.repo.TeacherRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService{
    private CourseRepo courseRepo;
    private StudentRepo studentRepo;
    private TeacherRepo teacherRepo;
    private EventRepo eventRepo;

    @Autowired
    public CourseService(CourseRepo courseRepo, 
                        StudentRepo studentRepo, 
                        TeacherRepo teacherRepo, 
                        EventRepo eventRepo) {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.eventRepo = eventRepo;
    }

    public List<Course> getCourses(String courseName){
        return (courseName == null) ? 
            courseRepo.findAll() : 
            courseRepo.findByNameContaining(courseName);
    }

    public Optional<Course> getCourseById(long id){
        return courseRepo.findById(id);
    }

    public Set<Student> getStudentsByCourseId(long id){
        return getCourseById(id).get().getStudents();
    }

    //Creating an entry in the studentcourse join table, adds a student to a course
    public boolean addStudentByIdToCourse(long studentId, long courseId){
        if(!studentRepo.existsById(studentId))
            return false;
        //adding course with id to student.getcourses collection
        Course c = courseRepo.getById(courseId);
        c.getStudents().add(studentRepo.getById(studentId));
        courseRepo.save(c);
        return true;
        //adding student with id to courses.getstudents collection
        // Student s = studentRepo.getById(studentId);
        // s.getCourses().add(courseRepo.getById(courseId));
        // studentRepo.save(s);
    }

    public Course createCourse(Course course){
        return courseRepo.save(course);
    }

    public Course updateCourse(long id, Course newCourse){
        if(courseRepo.existsById(id)){
            newCourse.setId(id);
            return courseRepo.save(newCourse);
        }
        return null;
    }

    public boolean addTeacherByIdToCourse(long teacherId, long courseId){
        if(!teacherRepo.existsById(teacherId))
            return false;
        Course c = getCourseById(courseId).get();
        c.setTeacher(teacherRepo.getById(teacherId));
        courseRepo.save(c);

        // Teacher t = teacherRepo.getById(teacherId);
        // t.setCourse(getCourseById(courseId).get());
        // teacherRepo.save(t);
        return true;
    }

    @Transactional
    public void deleteCourse(long id){
        //sets courseId in teacher entity to null before deleting course
        Optional<Teacher> teacher = teacherRepo.findByCourseId(id);
        if(teacher.isPresent()){
            teacher.get().setCourse(null);
            teacherRepo.save(teacher.get());
        }
        eventRepo.deleteByCourseId(id);
        courseRepo.deleteById(id);
    }

    public void deleteAllCourses(){
        //sets courseID in all teachers to null and deletes courses
        for(Teacher t : teacherRepo.findAll()){
            t.setCourse(null);
            teacherRepo.save(t);
        }
        courseRepo.deleteAll();
    }
}
