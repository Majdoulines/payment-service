package com.marketplace.payment_service.entity;

import com.marketplace.payment_service.enums.PaymentStatus;
import com.marketplace.payment_service.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Paiement d'une commande (table "payments").
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long buyerId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal itemAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    /** Montant paye par l'acheteur = article + livraison + taxe. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** Commission de la plateforme (RG03). */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal feeAmount;

    /** Montant du au vendeur = article + livraison - commission (RG04). */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal netAmount;

    /** Cumul des remboursements deja effectues. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Column(length = 2)
    private String shippingState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    /** Jeton de carte fourni par le PSP (jamais le numero de carte). */
    private String paymentToken;

    /** Reference de la transaction chez le PSP. */
    private String pspReference;

    @Column(nullable = false, unique = true, length = 64)
    private String idempotencyKey;

    private String failureReason;

    /** Versement qui a inclus ce paiement. */
    private Long payoutId;

    private Instant shippedAt;
    private Instant deliveredAt;
    private Instant releasedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    /** Verrouillage optimiste : evite deux modifications simultanees (ENF06). */
    @Version
    private Long version;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    /**
     * Change le statut en verifiant que la transition est autorisee.
     * Toute transition non prevue leve 409 INVALID_STATUS_TRANSITION.
     */
    public void transitionTo(PaymentStatus target) {
        if (status != null && !status.canTransitionTo(target)) {
            throw BusinessException.conflict("INVALID_STATUS_TRANSITION",
                    "Transition interdite : " + status + " -> " + target);
        }
        this.status = target;
    }

    /** Part du vendeur restante apres remboursements partiels. */
    public BigDecimal sellerShare() {
        return netAmount.subtract(refundedAmount);
    }

    /** Montant encore remboursable a l'acheteur. */
    public BigDecimal refundableTotal() {
        return totalAmount.subtract(refundedAmount);
    }
}