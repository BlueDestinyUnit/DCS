package com.scd.dcs.mappers;

import com.scd.dcs.config.websocket.domains.ChatMessage;
import com.scd.dcs.config.websocket.domains.ChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface ChatMapper {
    int insertMessage(ChatMessage chatMessage);

    int insertChatRoom(ChatRoom chatRoom);

    List<ChatRoom> selectChatRooms();

    ChatRoom selectChatRoomByIndex(@RequestParam("index") String index);

    ChatRoom selectChatRoomByName(String name);

    int deleteChatRoomByIndex(int index);
}
