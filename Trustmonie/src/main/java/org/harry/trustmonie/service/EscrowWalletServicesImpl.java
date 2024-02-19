package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.EscrowWallet;
import org.harry.trustmonie.data.repository.EscrowWalletRepository;
import org.harry.trustmonie.exceptions.IllegalAccountException;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.harry.trustmonie.exceptions.InvalidAmountException;
import org.harry.trustmonie.exceptions.NegativeAmountException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EscrowWalletServicesImpl implements EscrowWalletServices{
    private final EscrowWalletRepository escrowWalletRepository;
    @Override
    public void depositToEscrowWallet(String walletAddress, String amount) throws NegativeAmountException {
        BigDecimal newAmount;
        try {
            newAmount = BigDecimal.valueOf(Long.parseLong(amount));
        } catch (Exception exception) {
            throw new InvalidAmountException("Invalid Amount");
        }

        EscrowWallet foundEscrowWallet = findEscrowWalletAddress(walletAddress);

        if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newBalance = foundEscrowWallet.getEscrowBalance();
            newBalance = newBalance.add(newAmount);
            foundEscrowWallet.setEscrowBalance(newBalance);
            escrowWalletRepository.save(foundEscrowWallet);
        } else {
            throw new NegativeAmountException("Negative amount cannot be deposited");
        }
    }

    @Override
    public BigDecimal withdrawFromEscrowWallet(String walletAddress, String amount) throws NegativeAmountException {
        BigDecimal newAmount;
        try {
            newAmount = BigDecimal.valueOf(Long.parseLong(amount));
        } catch(Exception exception){
            throw new InvalidAmountException("Invalid Amount");
        }

        EscrowWallet foundWallet = findEscrowWalletAddress(walletAddress);

        BigDecimal foundWalletBalance = foundWallet.getEscrowBalance();
        if (newAmount.compareTo(BigDecimal.ZERO) <= 0.0) {
            throw new NegativeAmountException("Invalid withdrawal amount: " + newAmount);
        }
        if (newAmount.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal newBalance = foundWallet.getEscrowBalance();
            newBalance = newBalance.subtract(newAmount);
            foundWallet.setEscrowBalance(newBalance);
            escrowWalletRepository.save(foundWallet);
            return newBalance;
        }else {
            throw new NegativeAmountException("Insufficient fund");
        }
    }


    @Override
    public EscrowWallet findEscrowWalletAddress(String escrowWalletAddress) throws IllegalAccountException {
        Optional<EscrowWallet> escrowWallet = escrowWalletRepository.findWalletByEscrowAccountNumber(escrowWalletAddress);
        System.out.println(escrowWallet);
        if(escrowWallet.isEmpty()) throw new IllegalAccountException("Account number not found");
        return escrowWallet.get();
    }
    @Override
    public EscrowWallet findEscrowWalletHolderByEscrowWalletAddress(String escrowWalletAddress) throws IllegalAccountException {
        Optional<EscrowWallet> escrowWallet = escrowWalletRepository.findEscrowWalletHolderByEscrowAccountNumber(escrowWalletAddress);
        if(escrowWallet.isEmpty()) throw new IllegalAccountException("Account name does not found.");
        return escrowWallet.get();

    }

    @Override
    public void escrowSave(EscrowWallet escrow) {
        escrowWalletRepository.save(escrow);
    }

    @Override
    public BigDecimal findEscrowBalanceByAccountNumber(String walletAddress) {
        Optional<EscrowWallet> escrowBalance = escrowWalletRepository.findBalanceByEscrowAccountNumber(walletAddress);

        if (escrowBalance.isPresent()) {
            return escrowBalance.get().getEscrowBalance();
        } else {
            throw new IllegalTransactionIdException("Balance not available: " + walletAddress);
        }
    }

    @Override
    public EscrowWallet findGeneralAccountNumberByEscrowAccountNumber(String businessEscrowAccountNumber) {
        Optional<EscrowWallet> generalAccountNumber = escrowWalletRepository.findGeneralAccountNumberByEscrowAccountNumber(businessEscrowAccountNumber);

        if (generalAccountNumber.isPresent()) {
            return generalAccountNumber.get();
        } else {
            throw new IllegalTransactionIdException("Account number not available: " + generalAccountNumber);
        }
    }


}

