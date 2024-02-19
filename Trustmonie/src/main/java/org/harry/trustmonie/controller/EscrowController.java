package org.harry.trustmonie.controller;

import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.DTOs.request.TransferRequest;
import org.harry.trustmonie.data.model.EscrowWallet;
import org.harry.trustmonie.exceptions.IllegalAccountException;
import org.harry.trustmonie.service.TranxactrustServices;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/escrow")
public class EscrowController {

    private final TranxactrustServices tranxactrustServices;

    @PostMapping("/deposit")
    public String depositEscrowWallet(@RequestBody String walletAddress, String amount){
        try {
            tranxactrustServices.depositEscrowWallet(walletAddress, amount);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/balance")
    public String getEscrowBalance(@RequestParam String accountNumber){
        try {
            BigDecimal balance = tranxactrustServices.getEscrowBalance(accountNumber);
            return "Balance is " + balance;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/reverse")
    public String reverseEscrowPayment(@RequestBody String transactionId){
        try {
            tranxactrustServices.reverseEscrowPayment(transactionId);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transfer")
    public String transferToEscrow(@RequestBody TransferRequest transferRequest){
        try {
            tranxactrustServices.transferToEscrow(transferRequest);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }
//    @GetMapping("/number")
//    public String getTransferToEscrowAccountNumber(@RequestBody TransferRequest transferRequest){
//        try {
//            String escrowAccountRequest = tranxactrustServices.getTransferToEscrowAccountNumber(transferRequest);
//            return "Escrow account number is " + escrowAccountRequest;
//        } catch (IllegalAccountException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/number")
    public String findEscrowAccount(@RequestBody String escrowWalletAddress){
        try {
            EscrowWallet account = tranxactrustServices.findEscrowAccount(escrowWalletAddress);
            return "Escrow wallet account number is " + account;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/cash-out")
    public String transferFromEscrow(@RequestBody String verifier){
        try {
            tranxactrustServices.transferFromEscrow(verifier);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/username")
    public String findEscrowAccountUserNameByEscrowWalletAddress(@RequestBody String escrowWalletAddress){
        try {
            EscrowWallet account = tranxactrustServices.findEscrowAccountUserNameByEscrowWalletAddress(escrowWalletAddress);
            return "Escrow wallet account user name is " + account;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/verifier")
    public String findPaymentVerificationCode(@RequestParam String verificationCode){
        try {
            String codeVerifier = tranxactrustServices.findPaymentVerificationCode(verificationCode).getVerificationCode();
            return "Payment verification code was found: " + codeVerifier;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/verify")
    public String paymentVerification_CodeVerifier(@RequestBody String verificationCode){
        try {
            tranxactrustServices.paymentVerification_CodeVerifier(verificationCode);
            return "Payment verification code verified successfully";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

}

