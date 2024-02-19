package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.EscrowTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscrowTransactionsRepository extends JpaRepository<EscrowTransactions, Long> {
    Optional<EscrowTransactions> findEscrowTransactionsByTransactionId(String transactionId);
    Optional<EscrowTransactions> findEscrowTransactionsByPaymentVerificationCode(String paymentVerificationCode);

    Optional<EscrowTransactions> findDepositAmountByTransactionId(String transactionId);

    Optional<EscrowTransactions> findEscrowReceiverAccountNameByTransactionId(String transactionId);
    Optional<EscrowTransactions> findTransactionStatusByTransactionId(String transactionId);
    Optional<EscrowTransactions> findAmountPaidByPaymentVerificationCode(String verifier);

    Optional<EscrowTransactions> findByPaymentVerificationCode(String verifier);
}

