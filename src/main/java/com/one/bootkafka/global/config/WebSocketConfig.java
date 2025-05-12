package com.one.bootkafka.global.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // "/topic"으로 시작하는 경로에 대해 메시지를 구독
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 메시지를 보낼 때 접두어
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws"로 연결하는 WebSocket 엔드포인트 설정
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
