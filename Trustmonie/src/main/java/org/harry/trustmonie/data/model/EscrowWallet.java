package org.harry.trustmonie.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.user.WalletHolder;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EscrowWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne
    private WalletHolder appUser;
    private String escrowAccountNumber;
    private String escrowAccountName;
    private String businessAccountNumber;
    private BigDecimal escrowBalance;
    private TransactionStatus status;
    private String depositAmount;
    @OneToMany(mappedBy = "escrowWallet", fetch = FetchType.EAGER)
    private List<EscrowTransactions> escrowTransactions;


    public EscrowWallet(String generatedEscrowWalletAddress, String escrowWalletHolder) {
        this.escrowAccountName = escrowWalletHolder;
        this.escrowAccountNumber = generatedEscrowWalletAddress;
    }

}

