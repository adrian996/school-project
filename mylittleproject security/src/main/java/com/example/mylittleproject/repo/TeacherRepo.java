package com.example.mylittleproject.repo;

import java.util.List;
import java.util.Optional;

import com.example.mylittleproject.model.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long>{
    List<Teacher> findByLastNameContaining(String name);
    Optional<Teacher> findByCourseId(long id);
    Optional<Teacher> findByEmail(String name);
}
