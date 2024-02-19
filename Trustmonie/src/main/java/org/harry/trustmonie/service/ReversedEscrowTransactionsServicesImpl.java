package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.ReversedEscrowTransactions;
import org.harry.trustmonie.data.model.TransactionStatus;
import org.harry.trustmonie.data.repository.ReversedEscrowTransactionsRepository;
import org.harry.trustmonie.exceptions.IllegalAmountException;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReversedEscrowTransactionsServicesImpl implements ReversedEscrowTransactionsServices{
    private final EscrowTransactionsServices escrowTransactionsServices;
    private final ReversedEscrowTransactionsRepository reversedEscrowTransactionsRepository;

    @Override
    public String findEscrowWalletTransactionId(String transactionId) throws IllegalTransactionIdException {
        String escrowTransactionsService = String.valueOf(escrowTransactionsServices.findEscrowReceiverAccountNumberByTransactionId(transactionId));
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return escrowTransactionsService;

    }

    @Override
    public void reversedEscrowTransactionsSave(ReversedEscrowTransactions escrowTransactions) {
        reversedEscrowTransactionsRepository.save(escrowTransactions);

    }

    @Override
    public void updateTransaction(EscrowTransactions depositTransaction) {

    }

    @Override
    public void saveReversedEscrowTransactions(ReversedEscrowTransactions reversalTransaction) {
        reversedEscrowTransactionsRepository.save(reversalTransaction);
    }

    @Override
    public ReversedEscrowTransactions findEscrowAccountNumberByTransactionId(String transactionId) {
        Optional<ReversedEscrowTransactions> reversedEscrowTransactions = reversedEscrowTransactionsRepository.findReversedEscrowTransactionsByOldTransactionId(transactionId);

        if (reversedEscrowTransactions.isPresent()) {
            return reversedEscrowTransactions.get();
        } else {
            throw new IllegalTransactionIdException("Escrow transaction not found for transactionId: " + transactionId);
        }
    }

    @Override
    public ReversedEscrowTransactions createReversalTransaction(EscrowTransactions depositTransaction) {
        ReversedEscrowTransactions reversalTransaction = new ReversedEscrowTransactions();
        String reversedAmount = depositTransaction.getReceiverAccountNumber(); //.negate();// Reverse the amount
        reversalTransaction.setAmountPaid(reversedAmount);
        reversalTransaction.setTransactionDate(LocalDateTime.now());
        reversalTransaction.setStatus(TransactionStatus.REVERSED);

        return reversalTransaction;
    }

    @Override
    public String findEscrowWalletAmount(String amount) throws IllegalAmountException {
        String escrowTransactionsService = escrowTransactionsServices.findEscrowWalletDepositAmount(amount);
        NetworkFailureServicesImpl networkFailureServices = new NetworkFailureServicesImpl();
        networkFailureServices.getAnyRequest();
        return escrowTransactionsService;

    }


}


