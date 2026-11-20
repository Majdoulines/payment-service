package com.marketplace.payment_service.entity;

import com.marketplace.payment_service.enums.RefundStatus;
import com.marketplace.payment_service.enums.RefundType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Remboursement (total ou partiel) d'un paiement (table "refunds").
 */
@Entity
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RefundType type;

    private String reason;

    /** SELLER ou ADMIN */
    @Column(nullable = false, length = 10)
    private String initiatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefundStatus status;

    private String pspRefundId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}