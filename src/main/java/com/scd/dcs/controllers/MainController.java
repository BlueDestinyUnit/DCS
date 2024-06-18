package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.mappers.AttendanceMapper;
import com.scd.dcs.results.Result;
import com.scd.dcs.results.user.AttendanceResult;
import com.scd.dcs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMain(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        if(authentication != null) {
            SecurityUser securityUser = (SecurityUser)authentication.getPrincipal();
            UserEntity user = securityUser.getUserEntity();
            AttendanceEntity attendance = this.userService.selectAttendance(user.getEmail());
            if(attendance == null) {
                this.userService.insertAttendance(user);
            }else{
                modelAndView.addObject("attendance", AttendanceResult.ATTENDANCE_IS_EXIST);
            }
        }
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
