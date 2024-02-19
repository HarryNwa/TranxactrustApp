package org.harry.trustmonie.service;

import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.data.model.WalletTransactions;

import java.math.BigDecimal;

public interface WalletTransactionsServices {
    Iterable<WalletTransactions> findAll();
    WalletTransactions findEscrowWalletTransactionID(String transactionID);

    void walletDepositTransactionsSave(WalletTransactions walletTransactions);

    void walletWithdrawalTransactionsSave(WalletTransactions walletTransactions);

    void transferToWalletSave(WalletTransactions walletTransactions);

    BigDecimal findBalanceByAccountNumber(String balance);

    TransactionStatus getTransactionStatus(String transactionID);
    BigDecimal findReceiverBalanceByAccountNumber(String accountNumber);

}

