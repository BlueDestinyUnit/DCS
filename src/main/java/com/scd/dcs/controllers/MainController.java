package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.ArticleEntity;
import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.Progress;
import com.scd.dcs.mappers.AttendanceMapper;
import com.scd.dcs.results.Result;
import com.scd.dcs.results.user.AttendanceResult;
import com.scd.dcs.services.ArticleService;
import com.scd.dcs.services.UserService;
import com.scd.dcs.services.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController {

    private final UserService userService;

    private final ArticleService articleService;

    private final WorkService workService;

    @Autowired
    public MainController(UserService userService, ArticleService articleService, WorkService workService) {
        this.articleService = articleService;
        this.userService = userService;
        this.workService = workService;
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
        
        // 작업량 개수 셀 때 admin 제외 + 일 별 개수량 평균
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String year = currentDate.format(formatter1);
        String day = currentDate.format(formatter2);
        Progress progress = this.workService.countSubmitImage();
        Progress progressOfYear = this.workService.countSubmitImage(year, false);
        Progress progressOfDay = this.workService.countSubmitImage(day, true);
        ArticleEntity article = this.articleService.mainNoticeArticle();
        System.out.println(article);
        modelAndView.addObject("progress", progress);
        modelAndView.addObject("progressOfYear", progressOfYear);
        modelAndView.addObject("article", article);
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
