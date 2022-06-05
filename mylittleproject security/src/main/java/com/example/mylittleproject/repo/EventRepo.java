package com.example.mylittleproject.repo;

import java.util.List;

import com.example.mylittleproject.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Event, Long>{
    public List<Event> findByCourseId(long id);
    public void deleteByCourseId(long id);
}
