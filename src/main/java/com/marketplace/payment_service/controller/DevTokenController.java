package com.marketplace.payment_service.controller;

import com.marketplace.payment_service.integration.UserGateway;
import com.marketplace.payment_service.integration.UserInfo;
import com.marketplace.payment_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * UNIQUEMENT en mode standalone : fabrique un jeton pour un utilisateur de test,
 * en remplacement du login de user-service.
 *
 * POST /api/dev/token/1  -> jeton d'Ali (acheteur)
 */
@RestController
@RequestMapping("/api/dev")
@Profile("standalone")
@RequiredArgsConstructor
public class DevTokenController {

    private final UserGateway userGateway;
    private final JwtService jwtService;

    @PostMapping("/token/{userId}")
    public Map<String, Object> token(@PathVariable Long userId) {
        UserInfo user = userGateway.getUser(userId);
        String token = jwtService.generateToken(user.id(), user.email(), user.role());
        return Map.of(
                "token", token,
                "userId", user.id(),
                "email", user.email(),
                "role", user.role()
        );
    }
}