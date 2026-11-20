package com.marketplace.payment_service.entity;

import com.marketplace.payment_service.enums.AccountStatus;
import com.marketplace.payment_service.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Compte de versement du vendeur : banque ou PayPal (table "payout_accounts").
 */
@Entity
@Table(name = "payout_accounts")
@Getter
@Setter
@NoArgsConstructor
public class PayoutAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AccountType type;

    /** Numero masque, ex. ****6789 (jamais le numero complet en clair). */
    private String maskedNumber;

    private String paypalEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    /** Compte principal : celui qui recoit les versements. */
    private boolean principal;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    /** Destination envoyee au PSP pour le virement. */
    public String destination() {
        return type == AccountType.PAYPAL ? paypalEmail : maskedNumber;
    }
}