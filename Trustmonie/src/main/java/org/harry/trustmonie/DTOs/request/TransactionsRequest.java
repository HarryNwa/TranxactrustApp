package org.harry.trustmonie.DTOs.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.harry.trustmonie.data.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionsRequest {
    private String transactionId;
    private String amount;
    private String accountNumber;
    private String receiverAmount;
    private String receiverAccountNumber;
    private String bank;
    private String accountName;
    private String payerName;
    private String paymentVerificationCode;
    private BigDecimal depositorBalance;
    private BigDecimal creditorBalance;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime transactionDate = LocalDateTime.now();

}
