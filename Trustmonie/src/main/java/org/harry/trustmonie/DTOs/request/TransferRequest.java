package org.harry.trustmonie.DTOs.request;

import lombok.Data;

@Data
public class TransferRequest {
    private String amount;
    private String from;
    private String to;
    private int pin;
}

