package com.scd.dcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getChat() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chat/list");
        return modelAndView;
    }
}

