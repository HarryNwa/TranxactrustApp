package org.harry.trustmonie.controller;

import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.DTOs.request.TransferRequest;
import org.harry.trustmonie.DTOs.request.WithdrawRequest;
import org.harry.trustmonie.data.model.Wallet;
import org.harry.trustmonie.exceptions.IllegalAccountException;
import org.harry.trustmonie.service.TranxactrustServices;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/wallet")
public class WalletController {
    private final TranxactrustServices tranxactrustServices;



    @GetMapping("/balance")
    public String getWalletBalance(@RequestParam String accountNumber, int pin){
        try {
            BigDecimal balance = tranxactrustServices.getWalletBalance(accountNumber, pin);
            return "Balance is " + balance;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/deposit")
    public String depositIntoWallet(@RequestBody String accountNumber, String amount){
        try {
            String transactionId = tranxactrustServices.depositIntoWallet(accountNumber, amount);
            return "Transaction successful. Transaction Id is " + transactionId;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/withdraw")
    public String withdrawFromWallet(@RequestBody WithdrawRequest withdrawRequest){
        try {
            tranxactrustServices.withdrawFromWallet(withdrawRequest);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transfer")
    public String transferToWallet(@RequestBody TransferRequest transferRequest){
        try {
            tranxactrustServices.transferToWallet(transferRequest);
            return "Transaction successful.";
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/number")
    public String findWalletAccount(@RequestBody String accNum){
        try {
            Wallet account = tranxactrustServices.findWalletAccount(accNum);
            return "Wallet account number is " + account;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/username")
    public String findWalletAccountByUsername(@RequestBody String accName){
        try {
            Wallet account = tranxactrustServices.findWalletAccountByUsername(accName);
            return "Wallet account user name is " + account;
        } catch (IllegalAccountException e) {
            throw new RuntimeException(e);
        }
    }
}

