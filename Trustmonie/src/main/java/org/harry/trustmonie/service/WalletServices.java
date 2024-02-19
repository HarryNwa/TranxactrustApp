package org.harry.trustmonie.service;

import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.Wallet;

import java.math.BigDecimal;

public interface WalletServices {
    BigDecimal walletDeposit(String walletAddress, String amount);
    String getWalletAddress();
    void withdrawFromWallet(String walletAddress, String amount, int password);
    int updatePassword(int oldPassword, int newPassword);
    String getWalletAccountInfo();
    String getWalletHolder();

    Wallet findWalletByAccountNumber(String accountName);
    Wallet findWalletWalletAccountNameByAccountNumber(String WalletHolder);

    void walletSave(Wallet wallet);
    void validatePassword(int pin);

    EscrowTransactions findEscrowDepositorAccountNumberByTransactionId(String receiver);

    BigDecimal findBalanceByAccountNumber(String accountNumber);

    void findPin(int pin);
}

