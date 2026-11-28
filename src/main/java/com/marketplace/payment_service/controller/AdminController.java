package com.marketplace.payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints d'administration (reserves au role ADMIN par SecurityConfig).
 * Complete a l'etape 10.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("message", "Acces administrateur autorise.");
    }
}