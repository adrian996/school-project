package com.example.mylittleproject.service;

import com.example.mylittleproject.model.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mylittleproject.repo.DateRepo;

@Service
public class DateService {
    private DateRepo dateRepo;
    
    @Autowired
    public DateService(DateRepo dateRepo) {
        this.dateRepo = dateRepo;
    }

    public List<Date> getDates(){
        return dateRepo.findAll();
    }

}
