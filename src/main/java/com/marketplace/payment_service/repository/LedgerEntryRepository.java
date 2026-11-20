package com.marketplace.payment.repository;

import com.marketplace.payment.entity.LedgerEntry;
import com.marketplace.payment.enums.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByPaymentIdOrderByIdAsc(Long paymentId);

    @Query("select coalesce(sum(e.amount), 0) from LedgerEntry e where e.direction = :direction")
    BigDecimal sumByDirection(@Param("direction") Direction direction);
}