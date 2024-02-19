package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.data.model.WalletTransactions;
import org.harry.trustmonie.data.repository.WalletTransactionsRepository;
import org.harry.trustmonie.exceptions.IllegalAmountException;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletTransactionsServicesImpl implements WalletTransactionsServices {
    @Autowired
    private WalletTransactionsRepository walletTransactionsRepository;
    @Override
    public WalletTransactions findEscrowWalletTransactionID(String transactionId) throws IllegalTransactionIdException {
        Optional<WalletTransactions> walletTransactions = walletTransactionsRepository.findEscrowWalletTransactionsByTransactionId(transactionId);
        if (walletTransactions.isEmpty()) throw new IllegalTransactionIdException("TransactionID not found");
        return walletTransactions.get();
    }

    @Override
    public void walletDepositTransactionsSave(WalletTransactions walletTransactions) {
        walletTransactionsRepository.save(walletTransactions);
    }

    @Override
    public void walletWithdrawalTransactionsSave(WalletTransactions walletTransactions) {
        walletTransactionsRepository.save(walletTransactions);
    }

    @Override
    public void transferToWalletSave(WalletTransactions walletTransactions) {
        walletTransactionsRepository.save(walletTransactions);
    }

    @Override
    public BigDecimal findBalanceByAccountNumber(String accountNumber) {
        Optional<WalletTransactions> walletTransactions = walletTransactionsRepository.findBalanceBySenderAccountNumber(accountNumber);
        if (walletTransactions.isEmpty()) throw new IllegalAmountException("Balance not available");
        return walletTransactions.get().getSenderBalance();
    }
    @Override
    public BigDecimal findReceiverBalanceByAccountNumber(String accountNumber) {
        Optional<WalletTransactions> walletTransactions = walletTransactionsRepository.findBalanceByReceiverAccountNumber(accountNumber);
        if (walletTransactions.isEmpty()) throw new IllegalAmountException("Balance not available");
        return walletTransactions.get().getReceiverBalance();


    }

    @Override
    public TransactionStatus getTransactionStatus(String transactionId) {
        Optional<WalletTransactions> walletTransactions = walletTransactionsRepository.findTransactionStatusByTransactionId(transactionId);

        if (walletTransactions.isPresent()) {
            return TransactionStatus.findStatus("Completed");
        } else {
            return TransactionStatus.findStatus("Declined");
        }
    }

    @Override
    public Iterable<WalletTransactions> findAll() {
        return walletTransactionsRepository.findAll();
    }

}

