package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.data.repository.EscrowTransactionsRepository;
import org.harry.trustmonie.exceptions.IllegalPaymentVerifierException;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EscrowTransactionsServicesImpl implements EscrowTransactionsServices {
    private final EscrowTransactionsRepository escrowTransactionsRepository;
    private final PaymentVerificationCodeServices paymentVerificationCodeServices;

    @Override
    public EscrowTransactions findEscrowReceiverAccountNumberByTransactionId(String transactionId) throws IllegalTransactionIdException {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findEscrowTransactionsByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for transactionId: " + transactionId);
        }
    }

    @Override
    public EscrowTransactions findEscrowReceiverAccountNumberByPaymentVerificationCode(String paymentVerificationCode) throws IllegalPaymentVerifierException {
        Optional<EscrowTransactions> businessEscrowAccountNumber = escrowTransactionsRepository.findEscrowTransactionsByPaymentVerificationCode(paymentVerificationCode);

        if (businessEscrowAccountNumber.isPresent()) {
            return businessEscrowAccountNumber.get();
        } else {
            throw new IllegalPaymentVerifierException("Escrow businessEscrowAccountNumber transaction not found for payment verification code: " + paymentVerificationCode);
        }
    }

    @Override
    public EscrowTransactions findAmountPaidByPaymentVerificationCode(String verifier) {
        Optional<EscrowTransactions> amountPaid = escrowTransactionsRepository.findAmountPaidByPaymentVerificationCode(verifier);

        if (amountPaid.isPresent()) {
            return amountPaid.get();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction amount not found for payment verification code: " + verifier);
        }
    }
    @Override
    public String findEscrowWalletDepositAmount(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findDepositAmountByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get().getSenderAmount();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for amount: " + transactionId);

        }

    }

    @Override
    public TransactionStatus getTransactionStatus(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findTransactionStatusByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return TransactionStatus.findStatus("Completed");
        } else {
            return TransactionStatus.findStatus("Declined");
        }
    }

    @Override
    public String findEscrowReceiverAccountNameByTransactionId(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findEscrowReceiverAccountNameByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get().getReceiverAccountName();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for amount: " + transactionId);

        }
    }

    @Override
    public String findEscrowAccountPayerNameByTransactionId(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findEscrowReceiverAccountNameByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get().getSenderAccountName();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction account name not found for transactionId: " + transactionId);

        }
    }

    @Override
    public void findByPaymentVerificationCode(String verifier) {
        Optional<EscrowTransactions> verificationCode = escrowTransactionsRepository.findByPaymentVerificationCode(verifier);

        if (verificationCode.isPresent()) {
            verificationCode.get();
        } else {
            throw new IllegalPaymentVerifierException("Payment verification code does not match: " + verificationCode);

        }
    }


    @Override
    public EscrowTransactions findEscrowAccountNumberByTransactionId(String transactionId) {
        Optional<EscrowTransactions> escrowTransactions = escrowTransactionsRepository.findEscrowTransactionsByTransactionId(transactionId);

        if (escrowTransactions.isPresent()) {
            return escrowTransactions.get();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for transactionId: " + transactionId);
        }
    }

    @Override
    public Iterable<EscrowTransactions> findAll() {
        return escrowTransactionsRepository.findAll();
    }

    @Override
    public void escrowDepositTransactionsSave(EscrowTransactions escrowTransactions) {
        escrowTransactionsRepository.save(escrowTransactions);

    }

    @Override
    public void transferToEscrowSave(EscrowTransactions escrowTransactions) {
        escrowTransactionsRepository.save(escrowTransactions);
    }

    @Override
    public void transferFromEscrowSave(EscrowTransactions escrowTransactions) {
        escrowTransactionsRepository.save(escrowTransactions);
    }

}

