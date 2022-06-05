package com.example.mylittleproject.repo;

import java.util.List;
import java.util.Optional;

import com.example.mylittleproject.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepo extends JpaRepository<Student, Long>{
    List<Student> findByLastNameContaining(String name);
    Optional<Student> findByEmail(String name);
    //List<Student> findByFirstNameContaining(String name);
}
