package com.scd.dcs.config.websocket;

import com.scd.dcs.config.websocket.handlers.DevLogWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket// 웹소켓 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    @Autowired
    private DevLogWebSocketHandler devLogWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocketHandler를 추가

        registry.addHandler(devLogWebSocketHandler, "/chating/{roomIndex}");
    }
}
// 출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
