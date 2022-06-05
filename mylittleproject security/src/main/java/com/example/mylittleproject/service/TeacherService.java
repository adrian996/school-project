package com.example.mylittleproject.service;

import java.util.List;
import java.util.Optional;

import com.example.mylittleproject.model.Course;
import com.example.mylittleproject.model.Teacher;
import com.example.mylittleproject.repo.CourseRepo;
import com.example.mylittleproject.repo.TeacherRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private TeacherRepo teacherRepo;
    private CourseRepo courseRepo;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo, CourseRepo courseRepo) {
        this.teacherRepo = teacherRepo;
        this.courseRepo = courseRepo;
    }

    public List<Teacher> getTeachers(String teacherName){
        return (teacherName == null) ? 
            teacherRepo.findAll(Sort.by("lastName").descending()) : 
            teacherRepo.findByLastNameContaining(teacherName);
    }

    public Optional<Teacher> getTeacherById(long id){
        return teacherRepo.findById(id);
    }

    public Optional<Teacher> getTeacherByEmail(String email){
        return teacherRepo.findByEmail(email);
    }

    public Teacher createTeacher(Teacher teacher){
        return teacherRepo.save(teacher);
    }

    public Teacher updateTeacher(long id, Teacher newTeacher){
        if(teacherRepo.existsById(id)){
            newTeacher.setId(id);
            return teacherRepo.save(newTeacher);
        }
        return null;
    }

    //Assigns a teacher to a course
    public boolean addCourseByIdToTeacher(long courseId, long teacherId){
        if(!courseRepo.existsById(courseId))
            return false;
        Teacher t = getTeacherById(teacherId).get();
        t.setCourse(courseRepo.getById(courseId));
        teacherRepo.save(t);
        //set course with courseId teacher object to teacher with teacherId
        Course c = courseRepo.getById(courseId);
        c.setTeacher(getTeacherById(teacherId).get());
        courseRepo.save(c);
        return true;
    }

    public void deleteTeacher(long id){
        //sets courseId in teacher entity to null before deleting course
        Optional<Course> course = courseRepo.findByTeacherId(id);
        if(course.isPresent()){
            course.get().setTeacher(null);
            courseRepo.save(course.get());
        }
        teacherRepo.deleteById(id);
    }

    public void deleteAllTeachers(){
        //sets teacherId in all courses to null and deletes teachers
        for(Course c : courseRepo.findAll()){
            c.setTeacher(null);
            courseRepo.save(c);
        }
        teacherRepo.deleteAll();
    }
}
