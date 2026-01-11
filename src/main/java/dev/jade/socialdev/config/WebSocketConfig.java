package dev.jade.socialdev.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/kdoc-api/spring-framework/org.springframework.messaging.simp.config/-message-broker-registry/index.html

        // configure prefixes to filter destinations to target the broker
        registry.enableSimpleBroker("/topic", "/queue");

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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                    if (sessionAttributes != null) {
                        String username = (String) sessionAttributes.get("username");
                        if (username != null) {
                            accessor.setUser(() -> username);
                        }
                    }
                }
                return message;
            }
        });
    }

    private static class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) {
            if (request instanceof ServletServerHttpRequest servletRequest) {
                HttpSession session = servletRequest.getServletRequest().getSession(false);
                if (session != null) {
                    attributes.put("username", session.getAttribute("username"));
                    attributes.put("userId", session.getAttribute("userId"));
                }
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
        }
    }
}
