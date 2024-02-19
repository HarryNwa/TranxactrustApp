package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.Wallet;
import org.harry.trustmonie.data.repository.EscrowTransactionsRepository;
import org.harry.trustmonie.data.repository.WalletRepository;
import org.harry.trustmonie.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletServicesImpl implements WalletServices{
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private EscrowTransactionsRepository escrowTransactionsRepository;

    private int PIN;

    @Override
    public BigDecimal walletDeposit(String walletAddress, String amount) throws NegativeAmountException, IllegalAccountException, InvalidAmountException {
        BigDecimal newAmount;
        try {
            newAmount = BigDecimal.valueOf(Long.parseLong(amount));
        } catch(Exception exception){
            throw new InvalidAmountException("Invalid Amount");
        }
        Wallet foundWallet = findWalletByAccountNumber(walletAddress);
        if (amount.compareTo(String.valueOf(BigDecimal.ZERO)) > 0) {
            BigDecimal newBalance = foundWallet.getWalletBalance();
            newBalance = newBalance.add(newAmount);
            foundWallet.setWalletBalance(newBalance);
            walletRepository.save(foundWallet);
        }
        else {
            throw new NegativeAmountException("Negative amount cannot be deposited");
        }
        return newAmount;
    }
    @Override
    public void validatePassword(int pin) throws InvalidPasswordException {
        String pinNumber = "^[0-9]{4}+$";
        String pins = String.valueOf(pin);
        if (!pins.matches(pinNumber)){
            throw new WrongPasswordException("Enter 4 digits pin number");
        }
    }

    @Override
    public EscrowTransactions findEscrowDepositorAccountNumberByTransactionId(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findEscrowTransactionsByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for transactionId: " + transactionId);
        }
    }

    @Override
    public BigDecimal findBalanceByAccountNumber(String accountNumber) {
        Optional<Wallet> walletBalance = walletRepository.findBalanceByGeneralAccountNumber(accountNumber);
        if (walletBalance.isPresent()) {
            return walletBalance.get().getWalletBalance();
        } else {
            throw new IllegalTransactionIdException("Balance not available: " + walletBalance);
        }
    }

    @Override
    public void findPin(int pin) {
        Optional<Wallet> walletPin = walletRepository.findPinByPin(pin);
        if (walletPin.isPresent()) {
            walletPin.get();
        } else {
            throw new WrongPasswordException("Wrong pin: " + pin);
        }
    }

    @Override
    public String getWalletAddress() {
        Wallet wallet = new Wallet();
        return wallet.getGeneralAccountNumber();
    }

    @Override
    public void withdrawFromWallet(String walletAddress, String amount, int password) throws InvalidPasswordException, NegativeAmountException, InvalidAmountException {
        BigDecimal newAmount = BigDecimal.ZERO;
        try {
            newAmount = BigDecimal.valueOf(Long.parseLong(amount));
        } catch(Exception exception){
            throw new InvalidAmountException("Invalid Amount");
        }
        Wallet wallet = findWalletByAccountNumber(walletAddress);

        validatePassword(password);

        if (amount.compareTo(String.valueOf(BigDecimal.ZERO)) <= 0) {
            throw new NegativeAmountException("Invalid withdrawal amount: " + amount);
        }
        if (newAmount.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal newBalance = wallet.getWalletBalance();
            newBalance = newBalance.subtract(newAmount);
            wallet.setWalletBalance(newBalance);
            walletRepository.save(wallet);
        }
        else {
            throw new NegativeAmountException("Insufficient fund");
        }
    }

    @Override
    public int updatePassword(int oldPassword, int newPassword) {
        if (PIN == oldPassword) {
            PIN = newPassword;
        }
        return newPassword;
    }

    @Override
    public String getWalletAccountInfo() {
        return "Account number: " + ((Wallet) walletRepository).getGeneralAccountNumber() +
                "\nAccount name: " + ((Wallet) walletRepository).getAccountName();
    }

    @Override
    public String getWalletHolder() {
        return ((Wallet) walletRepository).getAccountName();
    }


    @Override
    public Wallet findWalletByAccountNumber(String accountNumber) throws IllegalAccountException {

        Optional<Wallet> wallet = walletRepository.findWalletByGeneralAccountNumber(accountNumber);
        System.out.println(wallet);
        if(wallet.isEmpty()) throw new IllegalAccountException("Account number does not exist");
        return wallet.get();
    }
    @Override
    public Wallet findWalletWalletAccountNameByAccountNumber(String walletAddress) throws IllegalAccountException {
        Optional<Wallet> wallet = walletRepository.findWalletWalletAccountNameByGeneralAccountNumber(walletAddress);
        System.out.println(wallet);
        if(wallet.isEmpty()) throw new IllegalAccountException("Account does not exist");
        return wallet.get();

    }

    @Override
    public void walletSave(Wallet wallet) {
        walletRepository.save(wallet);
    }
}

