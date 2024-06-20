package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.WorkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping()
public class WorkController {
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }



    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteIndex(@RequestParam("indexArray") int[] indexArray){
        Result<?> result = this.workService.delete(indexArray);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    @RequestMapping(value = "/work", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWork(
            Authentication authentication,
            @RequestParam(value = "date",required = false) String date,
            SubmitImageEntity submitImageEntity
    ){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("work/mosaic");
        modelAndView.addObject("date", date);
        return modelAndView;
    }
    

    @RequestMapping(value = "/work/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SubmitImageEntity[] getWorkList(@RequestParam(value = "date",required = false) String date, Authentication authentication) throws IOException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
//        UserEntity user = new UserEntity();
//        user.setEmail("lsg9134@gmail.com");


//         서비스에서 전체 목록 들고오기
        return workService.imageList(user.getEmail(),date);
    }





    @RequestMapping(value = "/addWork", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(Authentication authentication,
                            @RequestParam("date") String date,
                            @RequestParam("images") MultipartFile[] images
    ) throws IOException {
        SubmitImageEntity[] submitImageEntities = new SubmitImageEntity[images.length];
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
//        UserEntity user = new UserEntity();
//        user.setEmail("lsg9134@gmail.com");
//        user.setPassword("1234");
        workService.saveImage(user,images,date);


        for (int i = 0; i < images.length; i++) {
            submitImageEntities[i] = new SubmitImageEntity();
            submitImageEntities[i].setImageData(images[i].getBytes());
            submitImageEntities[i].setContentType(images[i].getContentType());
        }

        JSONObject responseObject = new JSONObject();
        responseObject.put("result", "success");
        return responseObject.toString();
    }
    // 24-05-29 재 수정

    @RequestMapping(value = "/subImage", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@RequestParam("index") int index) {
        SubmitImageEntity image = this.workService.getImage(index);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getImageData().length)
                .body(image.getImageData());
    }

    @RequestMapping(value = "/updateImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex(@RequestParam(value = "images",required = false) MultipartFile[] images,
                             @RequestParam(value = "index",required = false) int index,
                             @RequestParam(value = "dragCount",required = false) int dragCount) throws IOException {

        SubmitImageEntity submitImageEntity = this.workService.getImage(index);
        JSONObject responseObject = new JSONObject();
        try{
            if (images != null && images.length > 0) {
                MultipartFile image = images[0];
                submitImageEntity.setImageData(image.getBytes());
                submitImageEntity.setOriginalName(image.getOriginalFilename());
                submitImageEntity.setContentType(image.getContentType());

            }
        }catch (Exception e) {
            responseObject.put("result",CommonResult.FAILURE.name().toLowerCase());
        }
        if(dragCount > 0 && submitImageEntity.isMosaic() == false) {
             submitImageEntity.setMosaic(true);
        }
        Result<?> result = workService.updateImage(submitImageEntity);

        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }




}
