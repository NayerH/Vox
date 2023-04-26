package com.vox.post.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CacheService {

    public LocalDate getCurrentDate() {
        return LocalDate.now().minus(1, java.time.temporal.ChronoUnit.MONTHS);
    }

}
