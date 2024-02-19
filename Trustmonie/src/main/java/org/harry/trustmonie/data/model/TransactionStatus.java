package org.harry.trustmonie.data.model;

import lombok.Getter;
import org.harry.trustmonie.exceptions.TransactionStatusException;

@Getter
public enum TransactionStatus {
    PENDING ("Pending"),
    COMPLETED ("Completed"),
    REVERSED ("Reversed"),
    FAILED ("Failed"),
    DECLINED("Declined"),
    ;
    private final String[] status;
    TransactionStatus (String... args){
        this.status = args;
    }

    public static TransactionStatus findStatus(String status) throws TransactionStatusException {
        for (TransactionStatus transactionStatus : TransactionStatus.values()){
            for (String newStatus : transactionStatus.getStatus()){
                if (newStatus.equals(status))
                    return transactionStatus;
            }
        }
        throw new TransactionStatusException("Status unavailable");
    }



}

