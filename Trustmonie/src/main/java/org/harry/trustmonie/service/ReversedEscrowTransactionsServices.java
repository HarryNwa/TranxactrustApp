package org.harry.trustmonie.service;

import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.ReversedEscrowTransactions;

import java.util.Optional;

public interface ReversedEscrowTransactionsServices {
    String findEscrowWalletTransactionId(String transactionID);


    void reversedEscrowTransactionsSave(ReversedEscrowTransactions escrowTransactions);

    String findEscrowWalletAmount(String amount);

    ReversedEscrowTransactions createReversalTransaction(EscrowTransactions escrowTransactions);


    void updateTransaction(EscrowTransactions depositTransaction);

    void saveReversedEscrowTransactions(ReversedEscrowTransactions reversalTransaction);

    ReversedEscrowTransactions findEscrowAccountNumberByTransactionId(String transactionId);


}

