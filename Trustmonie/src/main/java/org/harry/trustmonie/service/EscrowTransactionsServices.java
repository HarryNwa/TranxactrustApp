package org.harry.trustmonie.service;

import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;

public interface EscrowTransactionsServices {
    EscrowTransactions findEscrowReceiverAccountNumberByTransactionId(String transactionId) throws IllegalTransactionIdException;
    Iterable<EscrowTransactions> findAll();

    EscrowTransactions findEscrowReceiverAccountNumberByPaymentVerificationCode(String paymentVerificationCode);

    EscrowTransactions findAmountPaidByPaymentVerificationCode(String verifier);

    void escrowDepositTransactionsSave(EscrowTransactions escrowTransactions);

    void transferToEscrowSave(EscrowTransactions escrowTransactions);

    void transferFromEscrowSave(EscrowTransactions escrowTransactions);

    String findEscrowWalletDepositAmount(String transactionId);
    EscrowTransactions findEscrowAccountNumberByTransactionId(String receiver);

    TransactionStatus getTransactionStatus(String transactionId);

    String findEscrowReceiverAccountNameByTransactionId(String transactionId);

    String findEscrowAccountPayerNameByTransactionId(String transactionId);


    void findByPaymentVerificationCode(String verifier);
}

