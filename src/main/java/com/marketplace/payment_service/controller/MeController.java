package com.marketplace.payment_service.controller;

import com.marketplace.payment.security.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * GET /api/me : qui suis-je ? (verification du jeton)
 */
@RestController
@RequestMapping("/api")
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal AuthUser user) {
        return Map.of(
                "id", user.id(),
                "email", user.email(),
                "role", user.role()
        );
    }
}