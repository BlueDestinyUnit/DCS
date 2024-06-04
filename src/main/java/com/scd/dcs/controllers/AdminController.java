package com.scd.dcs.controllers;

import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.services.AdminService;
import com.scd.dcs.services.SalaryService;
import com.scd.dcs.services.UserService;
import org.springframework.aop.config.AdvisorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final SalaryService salaryService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, SalaryService salaryService, UserService userService) {
        this.adminService = adminService;
        this.salaryService = salaryService;
        this.userService = userService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSelectDate() {
        return new ModelAndView("admin/selectDate");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String postSelectDate() {
        return null;
    }

    @RequestMapping(value = "/attendance", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAttendance(@RequestParam("date") String date) {
        ModelAndView modelAndView = new ModelAndView();
        UserProperty[] userProperty = this.adminService.getUserProperty(date);
        modelAndView.addObject("userProperties", userProperty);
        // 1, user 서비스로 모든 유저를 들고온다.
        // 2. admin 서비스로 해당 date에 적합한 유저들을 검색한다.
        modelAndView.setViewName("admin/attendance");
        return modelAndView;
    }
}
