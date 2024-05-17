package com.scd.dcs.controllers;


import com.scd.dcs.domains.entities.CommentEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.CommentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
//@RequestMapping("/comment")
//public class CommentController {
//    private final CommentService commentService;
//
//    @Autowired
//    CommentController(CommentService commentService){
//        this.commentService = commentService;
//    }
//
//    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public List<CommentEntity> getIndex(@RequestParam("articleIndex") int articleIndex){
//        List<CommentEntity> commentEntities = this.commentService.getComments(articleIndex);
//        return commentEntities;
//    }
//
//    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String postIndex(@SessionAttribute(value = "user") UserEntity user, CommentEntity comment){
//        comment.setUserEmail(user.getEmail());
//        Result result = this.commentService.write(comment);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result",result.name().toLowerCase());
//        return jsonObject.toString();
//    }
//
//    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteIndex(@SessionAttribute(value = "user",required = false)UserEntity user, @RequestParam("index") int index){
//        Result result = this.commentService.deleteComment(index,user);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result",result.name().toLowerCase());
//        return jsonObject.toString();
//    }
//
//    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String patchIndex(@SessionAttribute(value = "user" ,required = false) UserEntity user,
//                             @RequestParam("index") int index,
//                             @RequestParam("newContent") String newContent){
//        Result result = this.commentService.modifyComment(user,index,newContent);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result",result.name().toLowerCase());
//        return jsonObject.toString();
//    }
//
//}
