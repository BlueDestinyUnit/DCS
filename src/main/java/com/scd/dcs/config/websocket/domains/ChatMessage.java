package com.scd.dcs.config.websocket.domains;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private int index;
    private String chatRoomIndex;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
}
