package org.harry.trustmonie.service;


import org.harry.trustmonie.data.model.TransactionsID;

public interface TransactionsIDServices {
    String generateTransactionsID();
    TransactionsID findTransactionID(String transactionID);
    String getTransactionID();
}

