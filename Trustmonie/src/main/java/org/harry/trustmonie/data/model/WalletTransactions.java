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
public class WalletTransactions {
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
    @ManyToOne
    private Wallet wallet;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private BigDecimal senderBalance;
    private BigDecimal receiverBalance;
    private LocalDateTime transactionDate = LocalDateTime.now();

}

