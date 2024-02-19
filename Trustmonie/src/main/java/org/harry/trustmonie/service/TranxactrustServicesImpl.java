package org.harry.trustmonie.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.DTOs.request.CreateAccountRequest;
import org.harry.trustmonie.DTOs.request.TransferRequest;
import org.harry.trustmonie.DTOs.request.WithdrawRequest;
import org.harry.trustmonie.data.model.*;
import org.harry.trustmonie.exceptions.*;
import org.harry.trustmonie.user.WalletHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
//@Builder
public class TranxactrustServicesImpl implements TranxactrustServices {
    private final WalletServices walletServices;
    private final EscrowWalletServices escrowWalletServices;
//    private final UserService userService;
    private final WalletTransactionsServices walletTransactionsServices;
    private final EscrowTransactionsServices escrowTransactionsServices;
    private final PaymentVerificationCodeServices verificationCodeServices;
    private final TransactionsIDServices transactionsIDServices;
    private final ReversedEscrowTransactionsServices reversedEscrowTransactionsServices;

    private final Wallet wallet = new Wallet();
    private final WalletHolder walletHolder = new WalletHolder();
    private final WalletTransactions walletTransactions = new WalletTransactions();
    private final EscrowTransactions escrowTransactions = new EscrowTransactions();
    private final ReversedEscrowTransactions reversedEscrowTransactions = new ReversedEscrowTransactions();
    private String generatedEscrowAccountNumber;
    private String verifier;


    @Override
    public String createDoubleWallet(CreateAccountRequest accountRequest) {
        nameValidation(accountRequest.getFirstName(), accountRequest.getMiddleName(), accountRequest.getLastName());
        String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();
        if (accountRequest.getMiddleName() == null){
            accountName = accountRequest.getFirstName() + " " + accountRequest.getLastName();
        }
        String walletAccountNumber = generateWalletAddress();
        generatedEscrowAccountNumber = generateWalletAddress();
        int pin = Integer.parseInt(pinValidation(accountRequest.getPassword()));

        BigDecimal escrowBalance = new BigDecimal(0);
        EscrowWallet escrowWallet = EscrowWallet.builder()
                .escrowBalance(escrowBalance)
                .businessAccountNumber(walletAccountNumber)
                .escrowAccountName(accountName)
                .escrowAccountNumber(generatedEscrowAccountNumber)
                .build();
        escrowWalletServices.escrowSave(escrowWallet);

        BigDecimal walletBalance = new BigDecimal(0);
        Wallet wallets = Wallet.builder()
                .walletBalance(walletBalance)
                .accountName(accountName)
                .generalAccountNumber(walletAccountNumber)
                .escrowAccountNumber(generatedEscrowAccountNumber)
                .pin(pin)
                .build();
        walletServices.walletSave(wallets);

        return walletAccountNumber;
    }

    @Override
    public String createSingleWallet(CreateAccountRequest accountRequest) {
        nameValidation(accountRequest.getFirstName(), accountRequest.getMiddleName(), accountRequest.getLastName());
        String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();
        if (accountRequest.getMiddleName() == null){
            accountName = accountRequest.getFirstName() + " " + accountRequest.getLastName();
        }
        String walletAccountNumber = generateWalletAddress();
        generatedEscrowAccountNumber = generateWalletAddress();
        int pin = Integer.parseInt(pinValidation(accountRequest.getPassword()));

        BigDecimal walletBalance = new BigDecimal(0);
        Wallet wallets = Wallet.builder()
                .walletBalance(walletBalance)
                .accountName(accountName)
                .generalAccountNumber(walletAccountNumber)
                .escrowAccountNumber(generatedEscrowAccountNumber)
                .pin(pin)
                .build();
        walletServices.walletSave(wallets);

        return walletAccountNumber;
    }

    @Override
    public String escrowWalletAddressGenerator() {
        return generatedEscrowAccountNumber;
    }

    @Override
    public void nameValidation(@NonNull String firstName, String middleName, String lastName) {
        String first = "^[a-zA-Z]+$";

        if (!firstName.matches(first) && !middleName.matches(first) && !lastName.matches(first)) {
            throw new IllegalNameFormatException("Name characters must be alphabets");

        }
    }

    @Override
    public String pinValidation(int pin){
        String pinNumber = "^[0-9]{4}+$";
        String pins = String.valueOf(pin);
        if (!pins.matches(pinNumber)){
            throw new WrongPasswordException("Enter 4 digits pin number");
        }
        return pins;
    }

    @Override
    public String generateWalletAddress() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder accountNumberBuilder = new StringBuilder(10);

        while (accountNumberBuilder.length() < 10) {
            String randomID = "21" + secureRandom.nextInt(900_000_000);
            accountNumberBuilder.append(randomID);
        }
        if (accountNumberBuilder.length() > 10) {
            accountNumberBuilder.delete(10, accountNumberBuilder.length());
        }
        return accountNumberBuilder.toString();
    }

    @Override
    public String generateTransactionID() {
        return transactionsIDServices.generateTransactionsID();
    }

    @Override
    public BigDecimal getWalletBalance(String accountNumber, int pin) throws IllegalAccountException {
        walletServices.findPin(pin);
        BigDecimal walletTransactionsBalance = walletServices.findBalanceByAccountNumber(accountNumber);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getBalanceRequest();
        return walletTransactionsBalance;

    }

    @Override
    public BigDecimal getEscrowBalance(String accountNumber) throws IllegalAccountException {
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getBalanceRequest();
        return findEscrowAccount(accountNumber).getEscrowBalance();
    }

    @Override
    public String depositIntoWallet(String accountNumber, String amount) throws IllegalAccountException, NegativeAmountException, InvalidAmountException {
        validateAccountNumber(accountNumber);
        String transactionID = generateTransactionID();
        walletServices.walletDeposit(accountNumber, amount);

        BigDecimal walletBalance = findWalletAccount(accountNumber).getWalletBalance();
        String accountName = String.valueOf(walletServices.findWalletWalletAccountNameByAccountNumber(accountNumber).getAccountName());

        walletTransactions.setReceiverAccountNumber(accountNumber);
        walletTransactions.setReceiverAmount(amount);
        walletTransactions.setReceiverBalance(walletBalance);
        walletTransactions.setReceiverAccountName(accountName);
        walletTransactions.setTransactionId(transactionID);
        walletTransactionsServices.walletDepositTransactionsSave(walletTransactions);

        TransactionStatus status = walletTransactionsServices.getTransactionStatus(transactionID);
        walletTransactions.setStatus(status);
        walletTransactionsServices.walletDepositTransactionsSave(walletTransactions);


        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();
        return transactionID;
    }

    private void validateAccountNumber(String walletAddress) {
        findWalletAccount(walletAddress);
    }

    @Override
    public String depositEscrowWallet(String walletAddress, String amount) throws IllegalAccountException, NegativeAmountException {
        validateEscrowAccount(walletAddress);
        String transactionID = generateTransactionID();
        escrowWalletServices.depositToEscrowWallet(walletAddress, amount);
        BigDecimal balance = escrowWalletServices.findEscrowBalanceByAccountNumber(walletAddress);

        String name = findEscrowAccountUserNameByEscrowWalletAddress(walletAddress).getEscrowAccountName();

        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();
        String paymentVerificationCode = generatePaymentVerificationCode();
        escrowTransactions.setReceiverAccountNumber(walletAddress);
        escrowTransactions.setReceiverAmount(amount);
        escrowTransactions.setReceiverAccountName(name);
        escrowTransactions.setPaymentVerificationCode(paymentVerificationCode);
        escrowTransactions.setTransactionId(transactionID);
        escrowTransactions.setReceiverBalance(balance);

        PaymentVerificationCode verificationCode = new PaymentVerificationCode();
        verificationCode.setVerificationCode(paymentVerificationCode);

        escrowTransactionsServices.escrowDepositTransactionsSave(escrowTransactions);
        verificationCodeServices.senderVerificationCodeSave(verificationCode);

        TransactionStatus status = escrowTransactionsServices.getTransactionStatus(transactionID);
        escrowTransactions.setStatus(status);
        escrowTransactionsServices.escrowDepositTransactionsSave(escrowTransactions);
        return transactionID;
    }

    private void validateEscrowAccount(String escrowAccount) {
        findEscrowAccount(escrowAccount);
    }

    @Override
    public String generatePaymentVerificationCode() {
        return verificationCodeServices.generatePaymentVerificationCode();
    }

    @Override
    public String withdrawFromWallet(WithdrawRequest withdrawRequest) throws IllegalAccountException, InvalidPasswordException, NegativeAmountException, InvalidAmountException {
        String accountNumber = findWalletAccount(withdrawRequest.getAccNo()).getGeneralAccountNumber();
        walletServices.withdrawFromWallet(withdrawRequest.getAccNo(), withdrawRequest.getAmount(), withdrawRequest.getPin());
        BigDecimal walletBalance = findWalletAccount(accountNumber).getWalletBalance();
        String accountName = findWalletAccountByUsername(withdrawRequest.getAccNo()).getAccountName();
        String transactionID = generateTransactionID();


        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();

        walletTransactions.setSenderAccountNumber(accountNumber);
        walletTransactions.setSenderAmount(withdrawRequest.getAmount());
        walletTransactions.setSenderBalance(walletBalance);
        walletTransactions.setSenderAccountName(accountName);
        walletTransactions.setTransactionId(transactionID);
        walletTransactionsServices.walletWithdrawalTransactionsSave(walletTransactions);
        TransactionStatus status = walletTransactionsServices.getTransactionStatus(transactionID);
        walletTransactions.setStatus(status);
        walletTransactionsServices.walletWithdrawalTransactionsSave(walletTransactions);
        return transactionID;
    }

    @Override
    public String reverseEscrowPayment(String transactionId) throws IllegalAccountException, NegativeAmountException, IllegalTransactionIdException, IllegalAmountException, InvalidAmountException {

        String businessWalletAddress = escrowTransactionsServices.findEscrowReceiverAccountNumberByTransactionId(transactionId).getReceiverAccountNumber();
        String amountPaidToBusiness  = escrowTransactionsServices.findEscrowWalletDepositAmount(transactionId);
        String customerAccountNumber = escrowTransactionsServices.findEscrowAccountNumberByTransactionId(transactionId).getSenderAccountNumber();

        String businessAccountName = findEscrowAccountUserNameByEscrowWalletAddress(businessWalletAddress).getEscrowAccountName();
        String customerAccountName = findWalletAccountByUsername(customerAccountNumber).getAccountName();

        BigDecimal balance = escrowWalletServices.withdrawFromEscrowWallet(businessWalletAddress, amountPaidToBusiness);
        BigDecimal customerAccountBalance = walletServices.walletDeposit(customerAccountNumber, amountPaidToBusiness);

        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();
        String newTransactionId = generateTransactionID();

        reversedEscrowTransactions.setBusinessAccountNumber(businessWalletAddress);
        reversedEscrowTransactions.setAmountPaid(amountPaidToBusiness);
        reversedEscrowTransactions.setBusinessAccountBalance(balance);
        reversedEscrowTransactions.setBusinessAccountName(businessAccountName);
        reversedEscrowTransactions.setCustomerAccountNumber(customerAccountNumber);
        reversedEscrowTransactions.setAmountReversed(amountPaidToBusiness);
        reversedEscrowTransactions.setCustomerAccountBalance(customerAccountBalance);
        reversedEscrowTransactions.setCusterAccountName(customerAccountName);
        reversedEscrowTransactions.setOldTransactionId(transactionId);
        reversedEscrowTransactions.setNewTransactionId(newTransactionId);

        reversedEscrowTransactionsServices.reversedEscrowTransactionsSave(reversedEscrowTransactions);
        TransactionStatus reverseStatus = escrowTransactionsServices.getTransactionStatus(transactionId);
        reversedEscrowTransactions.setStatus(reverseStatus);
        reversedEscrowTransactionsServices.reversedEscrowTransactionsSave(reversedEscrowTransactions);
        return newTransactionId;
    }

    @Override
    public String transferToWallet(TransferRequest transferRequest) throws IllegalAccountException,
            InvalidPasswordException, NegativeAmountException, InvalidAmountException {
        String depositorAccountNumber = String.valueOf(findWalletAccount(transferRequest.getFrom()).getGeneralAccountNumber());
        walletServices.withdrawFromWallet(transferRequest.getFrom(), transferRequest.getAmount(), transferRequest.getPin());
        BigDecimal depositorBalance = findWalletAccount(depositorAccountNumber).getWalletBalance();
        String transactionID = generateTransactionID();
        String senderName = String.valueOf(walletServices.findWalletWalletAccountNameByAccountNumber(transferRequest
                .getFrom()).getAccountName());

        String receiverAccountNumber = String.valueOf(findWalletAccount(transferRequest.getTo()).getGeneralAccountNumber());
        walletServices.walletDeposit(transferRequest.getTo(), transferRequest.getAmount());
        BigDecimal receiverBalance = findWalletAccount(receiverAccountNumber).getWalletBalance();

        String receiverName = String.valueOf(walletServices.findWalletWalletAccountNameByAccountNumber(transferRequest
                .getTo()).getAccountName());

        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();

        walletTransactions.setSenderAccountNumber(depositorAccountNumber);
        walletTransactions.setSenderAmount(transferRequest.getAmount());
        walletTransactions.setSenderBalance(depositorBalance);
        walletTransactions.setSenderAccountName(senderName);
        walletTransactions.setReceiverAccountNumber(receiverAccountNumber);
        walletTransactions.setReceiverAmount(transferRequest.getAmount());
        walletTransactions.setReceiverBalance(receiverBalance);
        walletTransactions.setReceiverAccountName(receiverName);
        walletTransactions.setTransactionId(transactionID);

        walletTransactionsServices.transferToWalletSave(walletTransactions);

        TransactionStatus status = walletTransactionsServices.getTransactionStatus(transactionID);
        walletTransactions.setStatus(status);
        walletTransactionsServices.transferToWalletSave(walletTransactions);
        return transactionID;
    }

    @Override
    public String transferToEscrow(TransferRequest transferRequest) throws IllegalAccountException, InvalidPasswordException, NegativeAmountException, InvalidAmountException {
        String senderAccountNumber = findWalletAccount(transferRequest.getFrom()).getGeneralAccountNumber();
        walletServices.withdrawFromWallet(transferRequest.getFrom(), transferRequest.getAmount(), transferRequest.getPin());
        String senderName = String.valueOf(walletServices.findWalletWalletAccountNameByAccountNumber(transferRequest.getFrom()).getAccountName());
        String transactionID = generateTransactionID();
        BigDecimal senderBalance = findWalletAccount(senderAccountNumber).getWalletBalance();
        findEscrowAccount(transferRequest.getTo());
        escrowWalletServices.depositToEscrowWallet(transferRequest.getTo(), transferRequest.getAmount());
        String receiverName = String.valueOf(escrowWalletServices.findEscrowWalletHolderByEscrowWalletAddress(transferRequest.getTo()).getEscrowAccountName());
        BigDecimal receiverBalance = getEscrowBalance(transferRequest.getTo());

        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();
        verifier = generatePaymentVerificationCode();
        PaymentVerificationCode paymentVerifier = new PaymentVerificationCode();
        verificationCodeServices.transferToEscrowSave(paymentVerifier);

        escrowTransactions.setSenderAccountNumber(senderAccountNumber);
        escrowTransactions.setSenderAmount(transferRequest.getAmount());
        escrowTransactions.setSenderAccountName(senderName);
        escrowTransactions.setSenderBalance(senderBalance);
        escrowTransactions.setReceiverAccountNumber(transferRequest.getTo());
        escrowTransactions.setReceiverAmount(transferRequest.getAmount());
        escrowTransactions.setReceiverBalance(receiverBalance);
        escrowTransactions.setReceiverAccountName(receiverName);
        escrowTransactions.setTransactionId(transactionID);
        escrowTransactions.setPaymentVerificationCode(verifier);
        escrowTransactionsServices.transferToEscrowSave(escrowTransactions);


        TransactionStatus status = escrowTransactionsServices.getTransactionStatus(transactionID);
        escrowTransactions.setStatus(status);
        escrowTransactionsServices.transferToEscrowSave(escrowTransactions);
        return transactionID;
    }
    public String verificationCode(){
        return verifier;
    }

    @Override
    public String getTransferToEscrowAccountNumber(TransferRequest transferRequest) throws IllegalAccountException {
        String escrowAccountRequest = escrowWalletServices.findEscrowWalletAddress(transferRequest.getTo()).getEscrowAccountNumber();
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return escrowAccountRequest;
    }

    public String getPaymentVerifier() {
        return "\nYour Payment Verification Code (PVC) is: " + generatePaymentVerificationCode() +
                "\nYou are to safeguard this code until delivery is complete. " +
                "\nYou are required to release this PVC to your vendor for your purchase clearance and collection. " +
                "\nThank you for choosing Tranxactrust.";
    }

    @Override
    public String transferFromEscrow(String verifier) throws IllegalAccountException, NegativeAmountException {
        String businessEscrowAccountNumber = escrowTransactionsServices.findEscrowReceiverAccountNumberByPaymentVerificationCode
                (verifier).getReceiverAccountNumber();

        String amountPaidToBusiness = escrowTransactionsServices.findAmountPaidByPaymentVerificationCode
                (verifier).getReceiverAmount();

        String generalBusinessAccountNumber = escrowWalletServices.findGeneralAccountNumberByEscrowAccountNumber(businessEscrowAccountNumber).getBusinessAccountNumber();


        escrowWalletServices.withdrawFromEscrowWallet(businessEscrowAccountNumber, amountPaidToBusiness);

        String transactionID = generateTransactionID();
        BigDecimal senderBalance = getEscrowBalance(businessEscrowAccountNumber);
        String senderName = String.valueOf(escrowWalletServices.findEscrowWalletHolderByEscrowWalletAddress
                (businessEscrowAccountNumber).getEscrowAccountName());

        walletServices.walletDeposit(generalBusinessAccountNumber, amountPaidToBusiness);
        BigDecimal receiverBalance = walletServices.findBalanceByAccountNumber(generalBusinessAccountNumber);
        String receiverName = String.valueOf(walletServices.findWalletWalletAccountNameByAccountNumber
                (generalBusinessAccountNumber).getAccountName());

        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.makePaymentRequest();

        escrowTransactions.setSenderAccountNumber(businessEscrowAccountNumber);
        escrowTransactions.setSenderAmount(amountPaidToBusiness);
        escrowTransactions.setSenderBalance(senderBalance);
        escrowTransactions.setSenderAccountName(senderName);
        escrowTransactions.setReceiverAccountNumber(generalBusinessAccountNumber);
        escrowTransactions.setReceiverAmount(amountPaidToBusiness);
        escrowTransactions.setReceiverBalance(receiverBalance);
        escrowTransactions.setReceiverAccountName(receiverName);
        escrowTransactions.setTransactionId(transactionID);
        escrowTransactions.setPaymentVerificationCode(generatePaymentVerificationCode());
        escrowTransactionsServices.transferFromEscrowSave(escrowTransactions);

        TransactionStatus status = escrowTransactionsServices.getTransactionStatus(transactionID);
        escrowTransactions.setStatus(status);
        escrowTransactionsServices.transferFromEscrowSave(escrowTransactions);
        return transactionID;
    }

    @Override
    public Wallet findWalletAccount(String accNum){
        Wallet foundWallet = walletServices.findWalletByAccountNumber(accNum);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return foundWallet;
    }

    @Override
    public EscrowWallet findEscrowAccount(String escrowWalletAddress) {
        EscrowWallet foundEscrowWallet = escrowWalletServices.findEscrowWalletAddress(escrowWalletAddress);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return foundEscrowWallet;
    }

    @Override
    public Wallet findWalletAccountByUsername(String accName) throws IllegalAccountException {
        Wallet foundWallet = walletServices.findWalletByAccountNumber(accName);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return foundWallet;
    }

    @Override
    public EscrowWallet findEscrowAccountUserNameByEscrowWalletAddress(String escrowWalletAddress) throws IllegalAccountException {
        EscrowWallet escrowWallet = escrowWalletServices.findEscrowWalletHolderByEscrowWalletAddress(escrowWalletAddress);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return escrowWallet;
    }

    @Override
    public PaymentVerificationCode findPaymentVerificationCode(String verifier) throws IllegalPaymentVerifierException {
        PaymentVerificationCode paymentVerifier = verificationCodeServices.findPaymentVerificationCode(verifier);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return paymentVerifier;
    }

    @Override
    public String paymentVerification_CodeVerifier(String verifier) {
        String valediction = "";
        try {
            int count = 0;
            escrowTransactionsServices.findByPaymentVerificationCode(verifier);
            NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
            networkFailureServices.getAnyRequest();
            count++;
            if (count > 1) {
                throw new IllegalPaymentVerifierException("Payment already verified");
            }
            String message = "Payment code verified successfully";
            transferFromEscrow(verifier);

        } catch (IllegalPaymentVerifierException e) {
            throw new IllegalPaymentVerifierException(e.getMessage());
        } finally {
            valediction = "Thank you for trusting us!";

        }
        return valediction;
    }

}




