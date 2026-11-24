package com.marketplace.payment_service.repository;

import com.marketplace.payment_service.entity.PayoutAccount;
import com.marketplace.payment_service.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutAccountRepository extends JpaRepository<PayoutAccount, Long> {

    List<PayoutAccount> findBySellerId(Long sellerId);

    Optional<PayoutAccount> findFirstBySellerIdAndPrincipalTrueAndStatus(Long sellerId, AccountStatus status);
}