package org.harry.trustmonie.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private int pin;
    @ManyToOne
    private WalletHolder appUser;
    private String accountName;
    private String generalAccountNumber;
    private String escrowAccountNumber;
    private BigDecimal walletBalance;
    private String depositAmount;
    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private List<WalletTransactions> walletTransactions;

    public Wallet(String generatedAccountNumber, String accountName){
        this.generalAccountNumber = generatedAccountNumber;
        this.accountName = accountName;

    }

}

