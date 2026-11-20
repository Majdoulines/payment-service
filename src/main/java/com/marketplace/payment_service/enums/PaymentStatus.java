package com.marketplace.payment_service.enums;

import java.util.EnumSet;
import java.util.Set;

public enum PaymentStatus {
    EN_ATTENTE,
    SEQUESTRE,
    PARTIELLEMENT_REMBOURSE,
    LIBERE,
    VERSE,
    ECHOUE,
    EXPIRE,
    REMBOURSE,
    EN_LITIGE;

    /** Paiements qui "occupent" une commande : on ne peut pas la payer une deuxieme fois. */
    public static final Set<PaymentStatus> ACTIVE =
            EnumSet.of(SEQUESTRE, PARTIELLEMENT_REMBOURSE, LIBERE, VERSE, EN_LITIGE);

    /** Paiements dont l'argent est encore bloque chez la plateforme. */
    public static final Set<PaymentStatus> IN_ESCROW =
            EnumSet.of(SEQUESTRE, PARTIELLEMENT_REMBOURSE);

    /** Transitions autorisees (tableau "Transitions autorisees" de la SFD). */
    public boolean canTransitionTo(PaymentStatus target) {
        return switch (this) {
            case EN_ATTENTE -> target == SEQUESTRE || target == ECHOUE || target == EXPIRE;
            case SEQUESTRE, PARTIELLEMENT_REMBOURSE -> target == LIBERE
                    || target == REMBOURSE
                    || target == PARTIELLEMENT_REMBOURSE
                    || target == EN_LITIGE;
            case EN_LITIGE -> target == SEQUESTRE || target == REMBOURSE;
            case LIBERE -> target == VERSE;
            case VERSE -> target == REMBOURSE;
            case ECHOUE, EXPIRE, REMBOURSE -> false;
        };
    }
}
