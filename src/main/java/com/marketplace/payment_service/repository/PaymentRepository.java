package com.marketplace.payment_service.repository;

import com.marketplace.payment_service.entity.Payment;
import com.marketplace.payment_service.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    boolean existsByOrderIdAndStatusIn(Long orderId, Collection<PaymentStatus> statuses);

    Optional<Payment> findFirstByOrderIdAndStatusIn(Long orderId, Collection<PaymentStatus> statuses);

    List<Payment> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<Payment> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    List<Payment> findByStatusIn(Collection<PaymentStatus> statuses);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findAllByOrderByCreatedAtDesc();
}
