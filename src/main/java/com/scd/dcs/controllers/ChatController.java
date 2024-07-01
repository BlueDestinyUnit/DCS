package com.scd.dcs.controllers;

import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.config.websocket.domains.ChatRoom;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.services.ChatRoomService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatRoomService chatRoomService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getChat(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chat/list");
        List<ChatRoom> chatRooms = chatRoomService.selectRooms();
        System.out.println(chatRooms);

        modelAndView.addObject("rooms",this.chatRoomService.selectRooms());
        return modelAndView;
    }

    @RequestMapping(value = "/chatRoom", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView chat(@RequestParam(value = "index",required = false) int index,
                             Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chat/chatRoom");
        ChatRoom chatRoom = chatRoomService.findRoomByIndex(String.valueOf(index));
        modelAndView.addObject("index",index);
        modelAndView.addObject("user", user);
        modelAndView.addObject("chatRoom",chatRoom);
        return modelAndView;
    }

    @PostMapping("/createRoom")
    @ResponseBody
    public String createRoom(Authentication authentication,
                             @RequestParam(value = "roomName",required = false) String roomName) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        ChatRoom chatRoom = chatRoomService.createRoom(user.getEmail(),roomName);
        System.out.println(chatRoom);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index",chatRoom.getIndex());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/deleteRoom",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRoom(@RequestParam(value = "index") int index) {
        CommonResult result = chatRoomService.deleteRoom(index);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result.name().toLowerCase());
        return jsonObject.toString();
    }

    // 출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
}

