package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.domains.vos.WorkListRequest;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.AdminService;
import com.scd.dcs.services.SalaryService;
import com.scd.dcs.services.UserService;
import com.scd.dcs.services.WorkService;
import org.json.JSONObject;
import org.springframework.aop.config.AdvisorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final WorkService workService;
    private final SalaryService salaryService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, WorkService workService, SalaryService salaryService, UserService userService) {
        this.adminService = adminService;
        this.workService = workService;
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
        List<UserProperty> userProperties = this.adminService.getUserProperty(date);
        modelAndView.addObject("Users", userProperties);
        modelAndView.addObject("date", date);

        // 1, user 서비스로 모든 유저를 들고온다.
        // 2. admin 서비스로 해당 date에 적합한 유저들을 검색한다.
        modelAndView.setViewName("admin/attendance");
        return modelAndView;
    }

    @RequestMapping(value = "/workList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWorkList(@RequestParam("date") String date,
                                    @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView();
        SubmitImageEntity[] imageList = this.workService.imageList(email, date);
        UserEntity user = this.adminService.selectUser(email);
        modelAndView.addObject("imageList", imageList);
        modelAndView.addObject("user", user.getName());
        modelAndView.addObject("date", date);
        modelAndView.setViewName("admin/workList");
        return modelAndView;
    }


    @RequestMapping(value = "/workList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWorkList(@RequestBody WorkListRequest request) {
        List<Object> sendList = request.getSendList();
        Result<?> result = this.adminService.updateComment(sendList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result.name().toLowerCase());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/salary", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSalary() {
        return new ModelAndView("admin/salary");
    }
    @RequestMapping(value = "/salary2", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSalary2() {
        return new ModelAndView("admin/salary2");
    }

}
