package com.marketplace.payment.security;

/**
 * Utilisateur connecte, reconstruit a partir du JWT.
 * Disponible dans les controleurs avec @AuthenticationPrincipal AuthUser user.
 */
public record AuthUser(Long id, String email, String role) {

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}