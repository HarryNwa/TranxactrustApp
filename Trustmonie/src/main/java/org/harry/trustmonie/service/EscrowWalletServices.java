package org.harry.trustmonie.service;

import org.harry.trustmonie.data.model.EscrowWallet;

import java.math.BigDecimal;

public interface EscrowWalletServices {
    void depositToEscrowWallet(String walletAddress, String amount);
    BigDecimal withdrawFromEscrowWallet(String walletAddress, String amount);
    EscrowWallet findEscrowWalletAddress(String accountNum);
    EscrowWallet findEscrowWalletHolderByEscrowWalletAddress(String accName);

    void escrowSave(EscrowWallet escrow);

    BigDecimal findEscrowBalanceByAccountNumber(String walletAddress);

    EscrowWallet findGeneralAccountNumberByEscrowAccountNumber(String businessEscrowAccountNumber);
}

