package org.harry.trustmonie.DTOs.request;

import lombok.Data;

@Data
public class CashOutRequest {
    private String amount;
    private String from;
    private String to;
    private String verifier;
    private String transactionId;

}

