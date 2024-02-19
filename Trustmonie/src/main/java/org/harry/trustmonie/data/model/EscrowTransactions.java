package org.harry.trustmonie.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class EscrowTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String senderAmount;
    private String receiverAmount;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String receiverAccountName;
    private String senderAccountName;
    private String paymentVerificationCode;
    private BigDecimal senderBalance;
    private BigDecimal receiverBalance;
    @ManyToOne
    private EscrowWallet escrowWallet;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime transactionDate = LocalDateTime.now();

}


