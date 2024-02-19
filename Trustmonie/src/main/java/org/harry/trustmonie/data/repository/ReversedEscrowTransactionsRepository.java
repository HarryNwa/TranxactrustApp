package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.ReversedEscrowTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReversedEscrowTransactionsRepository extends JpaRepository<ReversedEscrowTransactions, Long> {
    Optional<ReversedEscrowTransactions> findReversedEscrowTransactionsByOldTransactionId(String transactionId);

}
