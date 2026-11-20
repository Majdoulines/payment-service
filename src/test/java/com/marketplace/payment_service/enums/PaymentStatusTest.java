package com.marketplace.payment_service.enums;

import org.junit.jupiter.api.Test;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verifie les transitions de statut de la SFD (chapitre "Cycle de vie d'un paiement").
 */
class PaymentStatusTest {

    @Test
    void transitionsAutorisees() {
        assertTrue(PaymentStatus.EN_ATTENTE.canTransitionTo(PaymentStatus.SEQUESTRE));
        assertTrue(PaymentStatus.EN_ATTENTE.canTransitionTo(PaymentStatus.ECHOUE));
        assertTrue(PaymentStatus.SEQUESTRE.canTransitionTo(PaymentStatus.LIBERE));
        assertTrue(PaymentStatus.SEQUESTRE.canTransitionTo(PaymentStatus.REMBOURSE));
        assertTrue(PaymentStatus.LIBERE.canTransitionTo(PaymentStatus.VERSE));
        assertTrue(PaymentStatus.EN_LITIGE.canTransitionTo(PaymentStatus.SEQUESTRE));
    }

    @Test
    void transitionsInterdites() {
        assertFalse(PaymentStatus.VERSE.canTransitionTo(PaymentStatus.SEQUESTRE));
        assertFalse(PaymentStatus.EN_ATTENTE.canTransitionTo(PaymentStatus.VERSE));
        assertFalse(PaymentStatus.ECHOUE.canTransitionTo(PaymentStatus.SEQUESTRE));
        assertFalse(PaymentStatus.REMBOURSE.canTransitionTo(PaymentStatus.LIBERE));
        assertFalse(PaymentStatus.LIBERE.canTransitionTo(PaymentStatus.SEQUESTRE));
    }

    @Test
    void statutsEnSequestre() {
        assertTrue(PaymentStatus.IN_ESCROW.contains(PaymentStatus.SEQUESTRE));
        assertTrue(PaymentStatus.IN_ESCROW.contains(PaymentStatus.PARTIELLEMENT_REMBOURSE));
        assertFalse(PaymentStatus.IN_ESCROW.contains(PaymentStatus.LIBERE));
    }
}