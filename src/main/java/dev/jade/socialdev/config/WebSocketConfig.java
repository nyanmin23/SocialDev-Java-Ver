package dev.jade.socialdev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/kdoc-api/spring-framework/org.springframework.messaging.simp.config/-message-broker-registry/index.html

        // configure prefixes to filter destinations to target the broker
        registry.enableSimpleBroker("topic", "user");

        // STOMP messages whose destination header begins with /app are routed to
        // @MessageMapping methods in @Controller classes (at Server end)
        // https://stackoverflow.com/questions/38323193/what-is-setapplicationdestinationprefixes-being-used-for
        registry.setApplicationDestinationPrefixes("/app");

        // https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/kdoc-api/spring-framework/org.springframework.messaging.simp.config/-message-broker-registry/set-user-destination-prefix.html
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // Endpoint WebSocket Client needs  to connect for WebSocket Handshake (with SockJS fallback)
        registry.addEndpoint("/ws-socialdev").withSockJS();
    }
}
