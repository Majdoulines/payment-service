package com.marketplace.payment_service.enums;

public enum LedgerAccount {
    /** Argent de l'acheteur (sa carte). */
    BUYER,
    /** Argent bloque par la plateforme (sequestre). */
    ESCROW,
    /** Solde du vendeur dans l'application. */
    SELLER_BALANCE,
    /** Commissions gagnees par la plateforme. */
    PLATFORM_REVENUE,
    /** Taxes a reverser a l'Etat. */
    TAX,
    /** Argent sorti vers le compte bancaire / PayPal du vendeur. */
    PAYOUT_OUT
}
