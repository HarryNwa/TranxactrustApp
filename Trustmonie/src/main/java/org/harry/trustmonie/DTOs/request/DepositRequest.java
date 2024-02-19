package org.harry.trustmonie.DTOs.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private String accountNumber;
    private String amount;
    private int pin;
}
