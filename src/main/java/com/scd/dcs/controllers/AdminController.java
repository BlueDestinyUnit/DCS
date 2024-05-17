package com.scd.dcs.controllers;

import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.services.AdminService;
import com.scd.dcs.services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("Admin")
public class AdminController {
    private final AdminService adminService;
    private final SalaryService salaryService;

    @Autowired
    public AdminController(AdminService adminService, SalaryService salaryService) {
        this.adminService = adminService;
        this.salaryService = salaryService;
    }
}
