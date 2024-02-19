package org.harry.trustmonie.DTOs.request;

import lombok.Data;

@Data
public class GetBalanceRequest {
    private String accountNumber;
    private int pin;
}

