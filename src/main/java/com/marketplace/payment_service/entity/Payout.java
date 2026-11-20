package com.marketplace.payment_service.entity;

import com.marketplace.payment_service.enums.PayoutStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Versement : virement de l'argent du vendeur vers son compte (table "payouts").
 */
@Entity
@Table(name = "payouts")
@Getter
@Setter
@NoArgsConstructor
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long payoutAccountId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayoutStatus status;

    private String pspTransferId;

    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant paidAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}