package com.scd.dcs.services;

import com.scd.dcs.config.websocket.domains.ChatMessage;
import com.scd.dcs.config.websocket.domains.ChatRoom;
import com.scd.dcs.mappers.ChatMapper;
import com.scd.dcs.results.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {
    private final ChatMapper chatMapper;

    @Autowired
    public ChatRoomService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    private Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();


    public List<ChatRoom> selectRooms (){
        return chatMapper.selectChatRooms();
    }


    @Transactional
    public ChatRoom createRoom(String userName,String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName, userName);
        chatRoom.setCreator(userName);
        chatRoom.setCreatedAt(LocalDateTime.now());
        System.out.println("인설트"+chatRoom);
        chatMapper.insertChatRoom(chatRoom);
        chatRoom.setUserName(userName);
        return chatRoom;
    }

    public ChatRoom findRoomByName(String roomName) {
        ChatRoom dbChatRoom =  chatMapper.selectChatRoomByName(roomName);
        return dbChatRoom;
    }

    public ChatRoom findRoomByIndex(String roomIndex) {
        ChatRoom dbChatRoom =  chatMapper.selectChatRoomByIndex(roomIndex);
        return dbChatRoom;
    }


    public Map<String, ChatRoom> getChatRooms() {
        return chatRooms;
    }


    @Transactional
    public void insertMessage(ChatRoom chatRoom, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomIndex(chatRoom.getIndex());
        chatMessage.setSender(chatRoom.getUserName());
        chatMessage.setMessage(message);
        chatMessage.setCreatedAt(LocalDateTime.now());
        System.out.println(chatMessage);
        this.chatMapper.insertMessage(chatMessage);
    }

    @Transactional
    public CommonResult deleteRoom(int index){
        return chatMapper.deleteChatRoomByIndex(index) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
