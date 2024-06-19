package com.scd.dcs.services;

import com.scd.dcs.mappers.SalaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    private final SalaryMapper salaryMapper;

    @Autowired
    public SalaryService(SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
    }

}
