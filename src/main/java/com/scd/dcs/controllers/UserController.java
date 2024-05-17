package com.scd.dcs.controllers;

import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postLogin(HttpSession session,
                                  UserEntity user){
        Result<?> result = this.userService.login(user);
        if(result == CommonResult.SUCCESS){
            session.setAttribute("user", user);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("result", result.name());
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "register", method= RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRegister(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postRegister(UserEntity user){
        Result<?> result = this.userService.register(user);
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("result", result.name());
        return modelAndView;
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getResetPassword(){
        return new ModelAndView("resetPassword");
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postResetPassword(UserEntity resetUser, @RequestParam(value = "newPassword") String newPassword){
        Result<?> result = this.userService.resetPassword(resetUser, newPassword);
        ModelAndView modelAndView = new ModelAndView("resetPassword");
        modelAndView.addObject("result", result.name());
        return modelAndView;
    }

    @RequestMapping(value = "findEmail", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getFindEmail(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("findEmail");
        return modelAndView;
    }

    @RequestMapping(value = "findEmail", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postFindEmail(UserEntity findEmailUser){
        Result<?> result = this.userService.findEmail(findEmailUser);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("result", result.name());
        if(result == CommonResult.SUCCESS){
            modelAndView.addObject("email", findEmailUser.getEmail());
        }
        modelAndView.setViewName("findEmail");
        return modelAndView;
    }

}
