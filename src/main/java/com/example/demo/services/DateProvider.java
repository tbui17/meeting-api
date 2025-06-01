package com.example.demo.services;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class DateProvider {

    public OffsetDateTime now(){
        return OffsetDateTime.now();
    }
}
