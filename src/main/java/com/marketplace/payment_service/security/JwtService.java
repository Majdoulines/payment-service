package com.marketplace.payment_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Lecture et verification des JWT.
 * La cle secrete doit etre LA MEME que celle de user-service (qui cree les jetons).
 *
 * Contenu du jeton : sub = email, userId = identifiant, role = ACHETEUR | VENDEUR | ADMIN
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    /** Retourne l'utilisateur si le jeton est valide, sinon Optional.empty(). */
    public Optional<com.marketplace.payment.security.AuthUser> parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object userId = claims.get("userId");
            Object role = claims.get("role");
            if (!(userId instanceof Number number) || role == null) {
                return Optional.empty();
            }
            return Optional.of(new com.marketplace.payment.security.AuthUser(number.longValue(), claims.getSubject(), role.toString()));

        } catch (JwtException | IllegalArgumentException e) {
            // signature fausse, jeton expire ou mal forme
            return Optional.empty();
        }
    }

    /**
     * Cree un jeton. Utilise seulement pour les tests et le mode standalone :
     * en mode microservices, c'est user-service qui cree les jetons.
     */
    public String generateToken(Long userId, String email, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }
}