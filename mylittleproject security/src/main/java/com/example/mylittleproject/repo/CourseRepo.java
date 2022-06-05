package com.example.mylittleproject.repo;

import java.util.List;
import java.util.Optional;

import com.example.mylittleproject.model.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long>{
    List<Course> findByNameContaining(String name);
    Optional<Course> findByTeacherId(long id);
}
