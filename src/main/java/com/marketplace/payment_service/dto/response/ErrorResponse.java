package com.marketplace.payment_service.dto.response;

import java.time.Instant;

/** Format standard des erreurs (chapitre "Gestion des erreurs" de la SFD). */
public record ErrorResponse(Instant timestamp, int status, String code, String message, String path) {
}
