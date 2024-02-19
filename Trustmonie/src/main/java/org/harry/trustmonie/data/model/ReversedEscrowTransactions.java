package org.harry.trustmonie.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class ReversedEscrowTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String newTransactionId;
    private String oldTransactionId;
    private String amountPaid;
    private String amountReversed;
    private String businessAccountNumber;
    private String customerAccountNumber;
    private String bank;
    private String businessAccountName;
    private String custerAccountName;
    private String paymentVerificationCode;
    private BigDecimal businessAccountBalance;
    private BigDecimal customerAccountBalance;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime transactionDate = LocalDateTime.now();

}


