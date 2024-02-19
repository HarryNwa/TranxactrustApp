package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.EscrowWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscrowWalletRepository extends JpaRepository<EscrowWallet, Long> {
    Optional<EscrowWallet> findWalletByEscrowAccountNumber(String accountNum);

    Optional<EscrowWallet> findEscrowWalletHolderByEscrowAccountNumber(String escrowWalletAddress);
    Optional<EscrowWallet> findBalanceByEscrowAccountNumber(String walletAddress);

    Optional<EscrowWallet> findGeneralAccountNumberByEscrowAccountNumber(String businessEscrowAccountNumber);


}

