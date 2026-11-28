package com.marketplace.payment_service.integration;

import com.marketplace.payment_service.exception.BusinessException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Simulation de user-service (profil "standalone").
 * Permet de developper et tester le Payment Service seul.
 */
@Component
@Profile("standalone")
public class FakeUserGateway implements UserGateway {

    private static final Map<Long, UserInfo> USERS = Map.of(
            1L, new UserInfo(1L, "Ali", "ali@test.com", "ACHETEUR", false),
            2L, new UserInfo(2L, "Karim", "karim@test.com", "VENDEUR", false),
            3L, new UserInfo(3L, "Admin", "admin@test.com", "ADMIN", false),
            4L, new UserInfo(4L, "Fraudeur", "banni@test.com", "ACHETEUR", true)
    );

    @Override
    public UserInfo getUser(Long userId) {
        UserInfo user = USERS.get(userId);
        if (user == null) {
            throw BusinessException.notFound("USER_NOT_FOUND", "Utilisateur introuvable : " + userId);
        }
        return user;
    }
}