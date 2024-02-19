package org.harry.trustmonie;

import org.harry.trustmonie.DTOs.request.*;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.data.model.Wallet;
import org.harry.trustmonie.data.repository.*;
import org.harry.trustmonie.exceptions.IllegalPaymentVerifierException;
import org.harry.trustmonie.exceptions.NegativeAmountException;
import org.harry.trustmonie.service.EscrowTransactionsServices;
import org.harry.trustmonie.service.TranxactrustServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TrustmonieApplicationTests {

        @Autowired
        TranxactrustServices tranxactrustServices;
        @Autowired
        TransactionsIDRepository transactionsIDRepository;
        @Autowired
        EscrowTransactionsRepository escrowTransactionsRepository;
        @Autowired
        EscrowWalletRepository escrowWalletRepository;
        @Autowired
        WalletRepository walletRepository;
        @Autowired
        WalletTransactionsRepository walletTransactionsRepository;
        @Autowired
        PaymentVerificationCodeRepository verificationCodeRepository;
        @Autowired
        EscrowTransactionsServices escrowTransactionsServices;
        Wallet wallet = new Wallet();
        EscrowTransactions escrowTransactions = new EscrowTransactions();
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        CashOutRequest cashOutRequest = new CashOutRequest();
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        TransferRequest transferRequest = new TransferRequest();



        @BeforeEach
        public void teardown(){
            walletRepository.deleteAll();
            escrowWalletRepository.deleteAll();
            walletTransactionsRepository.deleteAll();
        }

        @Test
        public void canCreateDoubleWallet() {
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            wallet.setAccountName(accountRequest.getFirstName());
            wallet.setAccountName(accountRequest.getMiddleName());
            wallet.setAccountName(accountRequest.getLastName());
            wallet.setPin(accountRequest.getPassword());

            tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));
            assertThat(escrowWalletRepository.count(), is(1L));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajame@gmail.com");
            accountRequest.setPassword(8080);

            wallet.setAccountName(accountRequest.getFirstName());
            wallet.setAccountName(accountRequest.getMiddleName());
            wallet.setAccountName(accountRequest.getLastName());
            wallet.setPin(accountRequest.getPassword());

            tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            assertThat(escrowWalletRepository.count(), is(2L));
        }

    @Test
    public void canCreateSingleWallet() {
        accountRequest.setFirstName("Harry");
        accountRequest.setMiddleName("Ekele");
        accountRequest.setLastName("Nwa");
        accountRequest.setEmail("harrynwa90@gmail.com");
        accountRequest.setPassword(9090);

        wallet.setAccountName(accountRequest.getFirstName());
        wallet.setAccountName(accountRequest.getMiddleName());
        wallet.setAccountName(accountRequest.getLastName());
        wallet.setPin(accountRequest.getPassword());

        tranxactrustServices.createSingleWallet(accountRequest);
        assertThat(walletRepository.count(), is(1L));

    }

        @Test
        public void canDepositToWallet(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            assertNotNull(tranxactrustServices.findWalletAccount(accountNumber));
            BigDecimal bigDecimal = BigDecimal.valueOf(0);
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(bigDecimal));

            tranxactrustServices.depositIntoWallet(accountNumber, "6000");

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(6000)));
            assertThrows(NegativeAmountException.class, ()-> tranxactrustServices.depositIntoWallet(accountNumber, "-6000"));

        }

        @Test
        public void canDepositToEscrowWallet(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();


            wallet.setAccountName(accountName);

            wallet.setPin(accountRequest.getPassword());

            tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            String escrowAddress = tranxactrustServices.escrowWalletAddressGenerator();
            assertNotNull(tranxactrustServices.findEscrowAccount(escrowAddress));
            BigDecimal bigDecimal1 = BigDecimal.valueOf(0);

            assertThat(tranxactrustServices.getEscrowBalance(escrowAddress), comparesEqualTo(bigDecimal1));
            tranxactrustServices.depositEscrowWallet(escrowAddress,  "6000");
            assertThat(tranxactrustServices.getEscrowBalance(escrowAddress), comparesEqualTo(BigDecimal.valueOf(6000)));
            assertThrows(NegativeAmountException.class, ()-> tranxactrustServices.depositEscrowWallet(escrowAddress,"-6000"));

        }
        @Test
        public void canTransferToWallet(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(accountNumber2);
            transferRequest.setPin(9090);

            tranxactrustServices.transferToWallet(transferRequest);
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getWalletBalance(accountNumber2, 8080), comparesEqualTo(BigDecimal.valueOf(5000)));

        }
        @Test
        public void canTransferToEscrow(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            String escrowAccountNumber2 = tranxactrustServices.escrowWalletAddressGenerator();

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(escrowAccountNumber2);
            transferRequest.setPin(9090);

            tranxactrustServices.transferToEscrow(transferRequest);
            String verifier = tranxactrustServices.generatePaymentVerificationCode();

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(5000)));
        }
        @Test
        public void canTransferFromEscrow(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();


            wallet.setAccountName(accountName);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            String escrowAccountNumber2 = tranxactrustServices.escrowWalletAddressGenerator();

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(escrowAccountNumber2);
            transferRequest.setPin(9090);

            String id = tranxactrustServices.transferToEscrow(transferRequest);
            String verifier = tranxactrustServices.verificationCode();

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(5000)));

            cashOutRequest.setAmount("5000");
            cashOutRequest.setVerifier(verifier);

            escrowTransactions.setSenderAmount(cashOutRequest.getAmount());
            escrowTransactions.setPaymentVerificationCode(cashOutRequest.getVerifier());

            String transactionId = tranxactrustServices.transferFromEscrow(verifier);
            TransactionStatus status = escrowTransactionsServices.getTransactionStatus(id);

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getWalletBalance(accountNumber2, 8080), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(0)));
            assertThat(TransactionStatus.findStatus("Completed"), is(TransactionStatus.COMPLETED));

        }

        @Test
        public void canVerifyCodeAnd_TransferFromEscrow(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();


            wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            String escrowAccountNumber2 = tranxactrustServices.escrowWalletAddressGenerator();

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(escrowAccountNumber2);
            transferRequest.setPin(9090);

            String id = tranxactrustServices.transferToEscrow(transferRequest);
            String verifier = tranxactrustServices.verificationCode();

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(5000)));

            cashOutRequest.setAmount("5000");
            cashOutRequest.setVerifier(verifier);

            escrowTransactions.setSenderAmount(cashOutRequest.getAmount());
            escrowTransactions.setPaymentVerificationCode(cashOutRequest.getVerifier());

            String transactionId = tranxactrustServices.paymentVerification_CodeVerifier(verifier);
            TransactionStatus status = escrowTransactionsServices.getTransactionStatus(id);

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getWalletBalance(accountNumber2, 8080), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(0)));
            assertThat(TransactionStatus.findStatus("Completed"), is(TransactionStatus.COMPLETED));

        }

        @Test
        public void canVerifyCodeAnd_TransferFromEscrowOnce(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();


            wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            String escrowAccountNumber2 = tranxactrustServices.escrowWalletAddressGenerator();

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(escrowAccountNumber2);
            transferRequest.setPin(9090);

            String id = tranxactrustServices.transferToEscrow(transferRequest);
            String verifier = tranxactrustServices.verificationCode();

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(5000)));

            cashOutRequest.setAmount("5000");
            cashOutRequest.setVerifier(verifier);

            escrowTransactions.setSenderAmount(cashOutRequest.getAmount());
            escrowTransactions.setPaymentVerificationCode(cashOutRequest.getVerifier());

            String transactionId = tranxactrustServices.paymentVerification_CodeVerifier(verifier);
            TransactionStatus status = escrowTransactionsServices.getTransactionStatus(id);

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getWalletBalance(accountNumber2, 8080), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(0)));
            assertThat(TransactionStatus.findStatus("Completed"), is(TransactionStatus.COMPLETED));


            assertThrows(IllegalPaymentVerifierException.class, ()-> tranxactrustServices.paymentVerification_CodeVerifier(verifier));

        }
        @Test
        public void canReverseEscrowPayment(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            accountRequest.setFirstName("James");
            accountRequest.setMiddleName("Junior");
            accountRequest.setLastName("Ikenna");
            accountRequest.setEmail("ikennajames@gmail.com");
            accountRequest.setPassword(8080);

            String accountName1 = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName1);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber2 = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(2L));
            String escrowAccountNumber2 = tranxactrustServices.escrowWalletAddressGenerator();

            transferRequest.setAmount("5000");
            transferRequest.setFrom(accountNumber);
            transferRequest.setTo(escrowAccountNumber2);
            transferRequest.setPin(9090);

            String transactionId = tranxactrustServices.transferToEscrow(transferRequest);
            String verifier = tranxactrustServices.generatePaymentVerificationCode();

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(5000)));

            String holder = tranxactrustServices.reverseEscrowPayment(transactionId);

            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));
            assertThat(tranxactrustServices.getEscrowBalance(escrowAccountNumber2), comparesEqualTo(BigDecimal.valueOf(0)));

        }
        @Test
        public void canWithdrawFromWallet(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();


            wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            withdrawRequest.setAmount("5000");
            withdrawRequest.setAccNo(accountNumber);
            withdrawRequest.setPin(9000);

            tranxactrustServices.withdrawFromWallet(withdrawRequest);
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));
        }
        @Test
        public void adminCanFindWalletTransactionByTransactionId(){
            accountRequest.setFirstName("Harry");
            accountRequest.setMiddleName("Ekele");
            accountRequest.setLastName("Nwa");
            accountRequest.setEmail("harrynwa90@gmail.com");
            accountRequest.setPassword(9090);

            String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();

            wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
            wallet.setPin(accountRequest.getPassword());

            String accountNumber = tranxactrustServices.createDoubleWallet(accountRequest);
            assertThat(walletRepository.count(), is(1L));

            String id = tranxactrustServices.depositIntoWallet(accountNumber, "10000");
            assertThat(tranxactrustServices.getWalletBalance(accountNumber,9090), comparesEqualTo(BigDecimal.valueOf(10000)));

            withdrawRequest.setAmount("5000");
            withdrawRequest.setAccNo(accountNumber);
            withdrawRequest.setPin(9000);

            tranxactrustServices.withdrawFromWallet(withdrawRequest);
            assertThat(tranxactrustServices.getWalletBalance(accountNumber, 9090), comparesEqualTo(BigDecimal.valueOf(5000)));

            String details = String.valueOf(walletTransactionsRepository.findEscrowWalletTransactionsByTransactionId(id));
            System.out.println("Payment details===> "+details);

        }
//    @Test
//    public void canGetAccurateTransactionStatus(){
//        accountRequest.setFirstName("Harry");
//        accountRequest.setMiddleName("Ekele");
//        accountRequest.setLastName("Nwa");
//        accountRequest.setEmail("harrynwa90@gmail.com");
//        accountRequest.setPassword(9090);
//
//        String accountName = accountRequest.getFirstName() + " " + accountRequest.getMiddleName() + " " + accountRequest.getLastName();
//
//
//        wallet.setAccountName(accountName);
//        wallet.setEmail(accountRequest.getEmail());
//        wallet.setPin(accountRequest.getPassword());
//
//        String accountNumber = tranxactrustServices.createWallet(accountRequest);
//        assertThat(walletRepository.count(), is(1L));
//
//        String id = tranxactrustServices.depositIntoWallet(accountNumber, "10000");
//        assertThat(TransactionStatus.findStatus(id), is(TransactionStatus.COMPLETED));
//    }
    }

