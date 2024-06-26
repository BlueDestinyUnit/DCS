package com.scd.dcs.controllers;

import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.domains.vos.WorkListRequest;
import com.scd.dcs.domains.vos.PaymentVo;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.AdminService;
import com.scd.dcs.services.SalaryService;
import com.scd.dcs.services.UserService;
import com.scd.dcs.services.WorkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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

    @RequestMapping(value = "/attendance", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAttendance(@RequestParam(value = "date", required = false) String date) {
        ModelAndView modelAndView = new ModelAndView();
        if (date == null || date.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = currentDate.format(formatter);
        }
        List<UserProperty> userProperties = this.adminService.getUserProperty(date);
        modelAndView.addObject("Users", userProperties);
        System.out.println(date);
        System.out.println(userProperties);
        modelAndView.addObject("date", date);

        // 1, user 서비스로 모든 유저를 들고온다.
        // 2. admin 서비스로 해당 date 에 적합한 유저들을 검색한다.
        modelAndView.setViewName("admin/attendance2");
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
        modelAndView.setViewName("admin/workList2");
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
    public ModelAndView getSalary(@RequestParam(value = "date", required = false) String date,
                                  @RequestParam(value = "option", required = false) String option) {

        if (date == null || date.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            date = currentDate.format(formatter);
        }

        ModelAndView modelAndView = new ModelAndView("admin/salary");

        System.out.println(option);
        if ("month".equals(option) || "year".equals(option)) {
            PaymentVo[] paymentList = adminService.selectWorkVoByOption(date, option);
            System.out.println(Arrays.toString(paymentList));
            modelAndView.addObject("selectDate", date);
            modelAndView.addObject("paymentList", paymentList);
            modelAndView.addObject("option", option);
        } else {
            // 기본 옵션이나 처리할 옵션이 없는 경우
            PaymentVo[] paymentList = adminService.selectWorkVo(date);
            modelAndView.addObject("selectDate", date);
            modelAndView.addObject("paymentList", paymentList);
//        }
            return modelAndView;

        }
        return modelAndView;
    }
}
