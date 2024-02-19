package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletTransactionsRepository extends JpaRepository<WalletTransactions, Long> {

    Optional<WalletTransactions> findBalanceByReceiverAccountNumber(String accountNumber);
    Optional<WalletTransactions> findBalanceBySenderAccountNumber(String accountNumber);

    Optional<WalletTransactions> findEscrowWalletTransactionsByTransactionId(String transactionId);

    Optional<WalletTransactions> findTransactionStatusByTransactionId(String transactionId);

}

