//package com.scd.dcs.config.websocket.handlers;
//
//import com.scd.dcs.config.websocket.domains.ChatRoom;
//import com.scd.dcs.services.ChatRoomService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.json.JSONParser;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.LinkedHashMap;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//@Slf4j
//@Component
//public class TestHandler extends TextWebSocketHandler {
//    private ConcurrentMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
//    private ConcurrentMap<String, String> userMap = new ConcurrentHashMap<>();
//    private final ChatRoomService chatRoomService;
//
//
//    @Autowired
//    public TestHandler(ChatRoomService chatRoomService) {
//        this.chatRoomService = chatRoomService;
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String uri = session.getUri().toString();
//        String roomIndex = uri.substring(uri.lastIndexOf('/') + 1);
//        log.info("{} 연결되었습니다.", session.getId());
//        sessionMap.put(session.getId(), session);
//        userMap.put(session.getId(), session.getPrincipal().getName());
//
//        ChatRoom chatRoom = chatRoomService.findRoomByIndex(roomIndex);
//        chatRoom.addSession(session);
//
//        JSONObject obj = new JSONObject();
//        obj.put("type", "getId");
//        obj.put("sessionId", session.getId());
//        session.sendMessage(new TextMessage(obj.toString()));
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        log.info("{} 연결이 종료되었습니다.", session.getId());
//        sessionMap.remove(session.getId());
//        userMap.remove(session.getId());
//
//        for (ChatRoom chatRoom : chatRoomService.selectRooms()) {
//            chatRoom.removeSession(session);
//        }
//    }
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String msg = message.getPayload();
//        JSONParser jsonParser = new JSONParser(msg);
//        LinkedHashMap<String, Object> obj = jsonParser.object();
//
//        log.info("Received message: {}", obj);
//
//        String roomIndex = (String) obj.get("roomIndex");
//        ChatRoom chatRoom = chatRoomService.findRoomByIndex(roomIndex);
//
//        if (chatRoom != null) {
//            String type = (String) obj.get("type");
//            String userName = userMap.get(session.getId());
//            chatRoom.setUserName(userName);
//
//            JSONObject response = new JSONObject();
//            response.put("type", type);
//            response.put("sessionId", session.getId());
//            response.put("userName", userName);
//            response.put("msg", obj.get("msg"));
//            response.put("roomIndex", roomIndex);
//
//            if ("open".equals(type)) {
//                chatRoom.addSession(session);
//            } else if ("message".equals(type)) {
//                chatRoomService.insertMessage(chatRoom, (String) obj.get("msg"));
//                chatRoom.handleMessage(new TextMessage(response.toString()));
//            }
//        } else {
//            log.warn("Chat room with roomIndex {} not found.", roomIndex);
//        }
//    }
//}
