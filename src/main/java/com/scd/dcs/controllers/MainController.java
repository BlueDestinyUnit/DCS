package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {


    @RequestMapping(value = "/main", method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMain(){


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
