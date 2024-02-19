package org.harry.trustmonie.service;

import org.harry.trustmonie.DTOs.request.CreateAccountRequest;
import org.harry.trustmonie.DTOs.request.TransferRequest;
import org.harry.trustmonie.DTOs.request.WithdrawRequest;
import org.harry.trustmonie.data.model.EscrowWallet;
import org.harry.trustmonie.data.model.PaymentVerificationCode;
import org.harry.trustmonie.data.model.Wallet;

import java.math.BigDecimal;

public interface TranxactrustServices {

    String createDoubleWallet(CreateAccountRequest accountRequest);
    String createSingleWallet(CreateAccountRequest accountRequest);
    String generateWalletAddress();
    String generateTransactionID();
    BigDecimal getWalletBalance(String accountNumber, int pin);
    BigDecimal getEscrowBalance(String accountNumber);

    String depositIntoWallet(String accountNumber, String amount);

    void nameValidation(String firstName, String middleName, String lastName);

    String depositEscrowWallet(String walletAddress, String amount);

    String paymentVerification_CodeVerifier(String verifier);

    String withdrawFromWallet(WithdrawRequest withdrawRequest);

    String reverseEscrowPayment(String transactionID) ;

    String transferToWallet(TransferRequest transferRequest);
    String transferToEscrow(TransferRequest transferRequest);
    String verificationCode();
    String getTransferToEscrowAccountNumber(TransferRequest transferRequest);

    String transferFromEscrow(String verifier);

    Wallet findWalletAccount(String accNum) ;
    Wallet findWalletAccountByUsername(String accName);
    EscrowWallet findEscrowAccount(String accountNum) ;
    PaymentVerificationCode findPaymentVerificationCode(String verifier);
    String generatePaymentVerificationCode();
    EscrowWallet findEscrowAccountUserNameByEscrowWalletAddress(String escrowWalletAddress);
    String escrowWalletAddressGenerator();
    String pinValidation(int pin);

}

