package com.marketplace.payment.repository;

import com.marketplace.payment.entity.Payout;
import com.marketplace.payment.enums.PayoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayoutRepository extends JpaRepository<Payout, Long> {

    List<Payout> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    List<Payout> findBySellerIdAndStatus(Long sellerId, PayoutStatus status);
}