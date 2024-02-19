package org.harry.trustmonie.DTOs.request;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String amount;
    private String accNo;
    private int pin;

}

