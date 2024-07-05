package com.scd.dcs.config.websocket.handlers;

import com.scd.dcs.config.websocket.domains.ChatRoom;
import com.scd.dcs.services.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]

@Slf4j
@Component
public class DevLogWebSocketHandler extends TextWebSocketHandler {

//    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>(); // 웹소켓 세션을 담아둘 맵
    private Map<String, String> userMap = new ConcurrentHashMap<>(); // 사용자
    private Map<Integer, List<WebSocketSession>> chatRoomMap = new ConcurrentHashMap<>();  // 방에 있는 세션들

    private final ChatRoomService chatRoomService;

    @Autowired
    public DevLogWebSocketHandler(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println();
        System.out.println("handleTextMessage");
        String msg = message.getPayload();
        JSONParser jsonParser = new JSONParser(msg);
        LinkedHashMap<String, Object> obj = jsonParser.object();
        String userName = userMap.get(session.getId());
        String roomIndex = String.valueOf(obj.get("roomIndex"));
        ChatRoom chatRoom = chatRoomService.findRoomByIndex(roomIndex);
        if(chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setIndex(roomIndex);
        }
        chatRoom.setUserName(userName);
        chatRoom.setRoomSessions(chatRoomMap);
        int roomCount = chatRoomMap.get(Integer.parseInt(roomIndex)).size();
        System.out.println(roomCount);
        String content = (String) obj.get("msg");
        userMap.put(session.getId(), userName);
        JSONObject response = new JSONObject();

        if (obj.get("type").equals("open")) {
            System.out.println("open");
            response.put("type", "open");
            response.put("sessionId", obj.get("sessionId"));
            response.put("userName", obj.get("userName"));
            System.out.println("테스트:"+ obj.get("userName"));
            response.put("msg", obj.get("msg"));
            response.put("roomIndex", roomIndex);
            response.put("roomCount",roomCount);
            TextMessage textMessage = new TextMessage(response.toString());
            chatRoom.handleMessage(session, textMessage);
        } else if (obj.get("type").equals("message")) {
            System.out.println("message");
            chatRoomService.insertMessage(chatRoom, content);
            chatRoom.handleMessage(session, message); // 채팅 방에서 메시지 처리

            // 클라이언트에게 다시 메시지를 보내는 예시
            response.put("type", "message");
            response.put("sessionId", obj.get("sessionId"));
            response.put("userName", obj.get("userName"));
            response.put("msg", obj.get("msg"));
            response.put("roomIndex", roomIndex);
            response.put("roomCount",roomCount);
        } else {
            log.warn("Chat room with roomId {} not found.", roomIndex);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결되었습니다.", session.getId());
        String roomIndex = session.getUri().getPath().substring(9);
        userMap.put(session.getId(),session.getPrincipal().getName() );
        List<WebSocketSession> webSocketSessionList;
        if(chatRoomMap.containsKey(Integer.parseInt(roomIndex))){
            webSocketSessionList = chatRoomMap.get(Integer.parseInt(roomIndex));
            webSocketSessionList.add(session);
            chatRoomMap.put(Integer.parseInt(roomIndex), webSocketSessionList);
        }else {
            webSocketSessionList = new ArrayList<>();
            webSocketSessionList.add(session);
            chatRoomMap.put(Integer.parseInt(roomIndex), webSocketSessionList);
        }
        int roomCount = webSocketSessionList.size();
        ChatRoom chatRoom = chatRoomService.findRoomByIndex(roomIndex);
        chatRoom.setUserName(session.getPrincipal().getName());

        System.out.println(chatRoom);
        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        obj.put("roomIndex", roomIndex);
        obj.put("userName",session.getPrincipal().getName());
        obj.put("roomCount",roomCount);
        log.info(obj.toString());
        session.sendMessage(new TextMessage(obj.toString()));

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결이 종료되었습니다. Reason: {}", session.getId(), status);
        String roomIndex = session.getUri().getPath().substring(9);

        // 세션 맵에서 종료된 세션 제거
        List<WebSocketSession> webSocketSessionList = chatRoomMap.get(Integer.parseInt(roomIndex));
        webSocketSessionList.remove(session);
        int roomCount = webSocketSessionList.size();
        // 남아 있는 모든 클라이언트에게 종료된 세션을 알리는 메시지 보내기
        JSONObject response = new JSONObject();
        response.put("type", "close");
        response.put("sessionId", session.getId());
        response.put("userName", userMap.get(session.getId())); // 종료된 사용자 이름
        response.put("roomCount",roomCount);
        userMap.remove(session.getId());

        TextMessage textMessage = new TextMessage(response.toString());

        // 남아 있는 모든 세션에게 메시지 보내기
        for (WebSocketSession sess : webSocketSessionList) {
            if (sess.isOpen()) {
                sess.sendMessage(textMessage);
            }
        }
    }

}
