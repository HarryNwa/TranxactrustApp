package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.TransactionsID;
import org.harry.trustmonie.data.repository.TransactionsIDRepository;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class TransactionsIDServicesImpl implements TransactionsIDServices {

    private final TransactionsIDRepository transactionsIDRepository;

    @Override
    public String generateTransactionsID() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder transactionIDBuilder = new StringBuilder(16);

        while (transactionIDBuilder.length() < 16) {
            int transactionID = 100_000_000 + secureRandom.nextInt(900_000_000);
            transactionIDBuilder.append(transactionID);
        }
        if (transactionIDBuilder.length() > 16) {
            transactionIDBuilder.delete(16, transactionIDBuilder.length());
        }
        return transactionIDBuilder.toString();
    }

    @Override
    public TransactionsID findTransactionID(String transactionID) throws IllegalTransactionIdException {
        for (TransactionsID transactionsID : transactionsIDRepository.findAll()) {
            if (transactionsID.getTransactionID().equals(transactionID.formatted())) {
                return transactionsID;
            } else {
                throw new IllegalTransactionIdException("TransactionID mismatch");
            }
        }
        return null;
    }
    @Override
    public String getTransactionID() {
        TransactionsID transactionsID = new TransactionsID();
        return transactionsID.getTransactionID();
    }
}

