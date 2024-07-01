package com.scd.dcs.config.websocket.domains;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
@ToString
public class ChatRoom {
    private static final Logger log = LoggerFactory.getLogger(ChatRoom.class);
    private String index;
    private String roomName;
    private String userName;
    private String creator;
    private boolean isSecret = false;
    private LocalDateTime createdAt;
//    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private Map<Integer, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    // Constructor, getters and setters

    public ChatRoom(String roomName, String userName) {
        this.roomName = roomName;
        this.userName = userName;
    }

    public ChatRoom() {
    }

    public void handleMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        List<WebSocketSession> roomList = roomSessions.get(Integer.parseInt(index));
        for(WebSocketSession s: roomList) {
            System.out.println(s.getPrincipal().getName());
            s.sendMessage(new TextMessage(msg));
        }
    }
}
