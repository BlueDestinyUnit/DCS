package com.scd.dcs.controllers;

import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.services.AdminService;
import com.scd.dcs.services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class AdminController {
    private final AdminService adminService;
    private final SalaryService salaryService;

    @Autowired
    public AdminController(AdminService adminService, SalaryService salaryService) {
        this.adminService = adminService;
        this.salaryService = salaryService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdmin() {
        return new ModelAndView("admin");
    }

    @RequestMapping(value = "/admin2", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdmin2() {
        return new ModelAndView("admin2");
    }
}
