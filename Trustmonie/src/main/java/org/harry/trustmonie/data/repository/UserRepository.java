package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.user.WalletHolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<WalletHolder, Long> {
    WalletHolder findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);
}



