package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findWalletByGeneralAccountNumber(String accountNumber);

    Optional<Wallet> findBalanceByGeneralAccountNumber(String accountNumber);

    Optional<Wallet> findWalletWalletAccountNameByGeneralAccountNumber(String accName);

    Optional<Wallet> findPinByPin(int pin);
}

