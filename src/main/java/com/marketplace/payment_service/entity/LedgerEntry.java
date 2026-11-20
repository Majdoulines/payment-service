package com.marketplace.payment_service.entity;

import com.marketplace.payment_service.enums.Direction;
import com.marketplace.payment_service.enums.LedgerAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Ligne du journal comptable (table "ledger_entries").
 * Les lignes ne sont jamais modifiees ni supprimees (RG18).
 */
@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@NoArgsConstructor
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifie l'operation : toutes les lignes d'une meme operation ont la meme reference. */
    @Column(nullable = false, length = 80)
    private String transactionRef;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LedgerAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Direction direction;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** Proprietaire du compte (acheteur ou vendeur), null pour les comptes de la plateforme. */
    private Long ownerId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}