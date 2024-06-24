package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.UserService;
import jakarta.mail.MessagingException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/login");
        return modelAndView;
    }


    @RequestMapping(value = "register", method= RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRegister(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegister(EmailAuthEntity emailAuth,
            UserEntity user){
        System.out.println(1);
        Result<?> result = this.userService.register(emailAuth, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }


    @RequestMapping(value = "/findEmail", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getFindEmail(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/findEmail");
        return modelAndView;
    }

    @RequestMapping(value = "/findEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postFindEmail(UserEntity findEmailUser){
        Result<?> result = this.userService.findEmail(findEmailUser);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if(result == CommonResult.SUCCESS){
            responseObject.put("email", findEmailUser.getEmail());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/registerEmail", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRegisterEmail(EmailAuthEntity emailAuth){
        Result<?> result = this.userService.verifyEmailAuth(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    @RequestMapping(value = "/registerEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegisterEmail(EmailAuthEntity emailAuth) throws NoSuchAlgorithmException, MessagingException {
        Result<?> result = this.userService.sendRegisterEmail(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if(result == CommonResult.SUCCESS){
            responseObject.put("salt", emailAuth.getSalt());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getResetPassword(){
        return new ModelAndView("user/resetPassword");
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postResetPassword(EmailAuthEntity emailAuth,
                                      UserEntity user){
        System.out.println(user);
        Result<?> result = this.userService.resetPassword(emailAuth, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    @RequestMapping(value = "/resetPasswordEmail", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchResetPasswordEmail(EmailAuthEntity emailAuth){
        Result<?> result = this.userService.verifyEmailAuth(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    @RequestMapping(value = "/resetPasswordEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postResetPasswordEmail(EmailAuthEntity emailAuth) throws NoSuchAlgorithmException, MessagingException {
        Result<?> result = this.userService.sendResetPasswordEmail(emailAuth);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if(result == CommonResult.SUCCESS){
            responseObject.put("salt", emailAuth.getSalt());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getEmail(UserEntity user){
        Result<?> result = this.userService.recoverEmail(user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if(result == CommonResult.SUCCESS){
            responseObject.put("email", user.getEmail());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/attendance", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAttendance(){
        return new ModelAndView("user/attendance");
    }

    @RequestMapping(value = "/attendance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCalendar(Authentication authentication, String endDate){
        if(authentication == null) {
            return "null";
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        List<UserProperty> dbList = this.userService.getAttendance(user.getEmail(), endDate);
//        List<AttendaceEventDto> dbList = this.userService.getAttendance(user.getEmail(), endDate);
        System.out.println(dbList);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("list",dbList);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/myPage", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyPage(){
        return new ModelAndView("user/myPage");
    }
}
