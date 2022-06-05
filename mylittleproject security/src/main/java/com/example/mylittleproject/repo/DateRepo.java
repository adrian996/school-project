package com.example.mylittleproject.repo;
import com.example.mylittleproject.model.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepo extends JpaRepository<Date, Long>{
    
}
